#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Полный парсер цветочных мутаций для Binnie's Botany
Версия 5.4 (правильная логика: steps и steps_selection)
"""
import json
import re
import csv
from collections import defaultdict, deque
from dataclasses import dataclass
from typing import Dict, List, Set, Optional, Tuple
import sys


# ============================================================================
# КЛАССЫ ДАННЫХ
# ============================================================================
@dataclass
class DyePath:
    """Путь получения уникального красителя"""
    color: str
    steps: int
    selection_steps: int
    chance: int
    parents: Tuple[str, str]

    def __lt__(self, other):
        if self.selection_steps != other.selection_steps:
            return self.selection_steps < other.selection_steps
        if self.steps != other.steps:
            return self.steps < other.steps
        return self.chance > other.chance

@dataclass
class Flower:
    """Цветок с именем и его цветом"""
    name: str
    color: str

    def __hash__(self):
        return hash((self.name, self.color))

    def __eq__(self, other):
        if isinstance(other, Flower):
            return self.name == other.name and self.color == other.color
        return False


@dataclass
class ColorMutation:
    """Мутация цвета"""
    parent1: str
    parent2: str
    result: str
    chance: int


@dataclass
class MutatronRecipe:
    """Рецепт мутатрона"""
    result: str
    parent1: str
    parent2: str
    chance: int


@dataclass
class ColorPath:
    """Путь получения цвета (красителя)"""
    color: str
    steps: int  # общее количество шагов
    steps_selection: int  # шаги уникальных красителей
    chance: int  # шанс последнего скрещивания
    parent1: str  # родитель 1 (цвет)
    parent2: str  # родитель 2 (цвет)

    def __lt__(self, other):
        # Сортировка: меньше steps_selection > меньше steps > выше шанс
        if self.steps_selection != other.steps_selection:
            return self.steps_selection < other.steps_selection
        if self.steps != other.steps:
            return self.steps < other.steps
        return self.chance > other.chance


@dataclass
class FlowerPath:
    """Путь получения цветка"""
    flower: Flower
    steps: int  # общее количество шагов
    steps_selection: int  # шаги уникальных красителей
    chance: int  # шанс последнего скрещивания (только для цветовых мутаций)
    parents: Optional[Tuple[Flower, Flower]] = None
    recipe_type: str = "color"  # "base", "mutatron" или "color"

    def __lt__(self, other):
        # Сортировка: меньше steps_selection > меньше steps > выше шанс
        if self.steps_selection != other.steps_selection:
            return self.steps_selection < other.steps_selection
        if self.steps != other.steps:
            return self.steps < other.steps
        return self.chance > other.chance


# ============================================================================
# ОСНОВНОЙ КЛАСС-ПАРСЕР
# ============================================================================

class BotanyFlowerParser:
    def __init__(self):
        # Цветовые мутации
        self.color_mutations: List[ColorMutation] = []
        self.color_graph: Dict[str, List[Tuple[str, int]]] = defaultdict(list)
        self.all_colors: Set[str] = set()

        # Мутации видов
        self.mutatron_recipes: List[MutatronRecipe] = []
        self.mutatron_by_result: Dict[str, MutatronRecipe] = {}

        # Словари
        self.flower_by_color: Dict[str, Flower] = {}
        self.flower_by_species: Dict[str, Flower] = {}
        self.russian_by_species: Dict[str, str] = {}
        self.species_by_russian: Dict[str, List[str]] = defaultdict(list)

        # Базовые цветы
        self.base_flowers: List[Flower] = []
        self.base_colors: Set[str] = set()
        self.base_species: Set[str] = set()

        # Цвета, доступные через мутатрон
        self.mutatron_colors: Set[str] = set()

        # Кеш для лучших путей цветов (color -> (steps, steps_selection, chance, parent1, parent2))
        self.best_color_paths: Dict[str, Tuple[int, int, int, str, str]] = {}

        # Цвета в читаемом виде
        self.color_names = {
            # Базовые цвета
            "YELLOW": "жёлтый",
            "RED": "красный",
            "DEEP_SKY_BLUE": "светло-синий",
            "MEDIUM_PURPLE": "светло-мраморный",
            "LAVENDER": "блекло-голубой",
            "VIOLET": "розовато-лиловый",
            "WHITE": "белый",
            "CRIMSON": "малиновый",
            "DARK_ORANGE": "красно-оранжевый",
            "THISTLE": "бледно-фиолетовый",
            "PLUM": "светло-розовато-лиловый",

            # Из мутатрона
            "SKY_BLUE": "небесно-голубой",
            "PINK": "бледно-розовый",
            "LIGHT_GRAY": "светло-серый",
            "MEDIUM_ORCHID": "светло-пурпурный",
            "HOT_PINK": "розовый",
            "PALE_PINK": "бледно-розовый",
            "PALE_BLUE": "бледно-голубой",
            "SLATE_BLUE": "грифельно-синий",
            "ROYAL_BLUE": "королевский синий",
            "DARK_SLATE_GRAY": "тёмно-зеленовато-голубой",
            "BLACK": "чёрный",
            "GOLD": "золотистый",

            # Уникальные красители (из вашего списка)
            "AQUAMARINE": "аквамарин (цвет морской волны)",
            "BROWN": "красно-коричневый",
            "BLUE": "глубоко-синий",
            "CADET_BLUE": "тускло-синий",
            "CHOCOLATE": "светло-коричневый",
            "CORAL": "коралловый",
            "CYAN": "голубой",
            "DARK_GOLDENROD": "тёмно-бронзовый",
            "DARK_GRAY": "серый",
            "DARK_GREEN": "тёмно-зелёный",
            "DARK_KHAKI": "темно-бежевый",
            "DARK_OLIVE_GREEN": "тёмно-оливковый",
            "DARK_SALMON": "тёмно-лососевый",
            "DARK_SEA_GREEN": "тускло-зелёный",
            "DARK_SLATE_BLUE": "тёмно-синевато-серый",
            "DARK_TURQUOISE": "бирюзовый (сине-зелёный)",
            "DARK_VIOLET": "фиолетовый",
            "DEEP_PINK": "ярко-розовый",
            "DIM_GRAY": "тёмно-серый",
            "DODGER_BLUE": "синий",
            "GOLDENROD": "бронзовый",
            "GRAY": "тускло-серый",
            "GREEN": "зелёный",
            "INDIAN_RED": "кирпично-красный",
            "INDIGO": "индиго (синий)",
            "KHAKI": "бежевый",
            "LEMON_CHIFFON": "бледно-жёлтый",
            "LIGHT_SEA_GREEN": "тёмно-бирюзовый",
            "LIGHT_STEEL_BLUE": "бледно-голубой",
            "LIME": "лимонный",
            "LIME_GREEN": "светло-зелёный",
            "MAGENTA": "фуксин (ярко-красный)",
            "MAROON": "тёмно-бордовый",
            "MEDIUM_AQUAMARINE": "светло-мятный",
            "MEDIUM_SEA_GREEN": "мятный",
            "MEDIUM_VIOLET_RED": "пурпурно-красный",
            "MISTY_ROSE": "бледно-розовый",
            "NAVY": "тёмно-тёмно-синий",
            "OLIVE": "тёмно-жёлтый",
            "OLIVE_DRAB": "оливковый",
            "ORANGE": "оранжевый",
            "PALE_GREEN": "бледно-зелёный",
            "PALE_TURQUOISE": "бледно-бирюзовый",
            "PALE_VIOLET_RED": "тускло-розовый",
            "PERU": "желтовато-коричневый",
            "PURPLE": "тёмно-фиолетовый",
            "ROSY_BROWN": "тускло-красный",
            "SALMON": "лососевый",
            "SANDY_BROWN": "бледно-коричневый",
            "SEA_GREEN": "тёмно-мятный",
            "SIENNA": "коричневый",
            "SLATE_GRAY": "синевато-серый",
            "SPRING_GREEN": "весенне-зеленый",
            "STEEL_BLUE": "синий со стальным оттенком",
            "TAN": "бледно-рыжевато-коричневый",
            "TEAL": "бирюзово-голубой",
            "TURQUOISE": "светло-бирюзовый",
            "WHEAT": "бледно-оранжевый",
            "YELLOW_GREEN": "жёлто-зелёный",
        }

        # Кеш для результатов цветков
        self.cache: Dict[str, Optional[FlowerPath]] = {}

    # ========================================================================
    # ЗАГРУЗКА ДАННЫХ
    # ========================================================================

    def load_color_mutations(self, filename: str):
        """Загружает цветовые мутации"""
        pattern = r'addMix\((\w+),\s*(\w+),\s*(\w+),\s*(\d+)\);'

        with open(filename, 'r', encoding='utf-8') as f:
            content = f.read()

        matches = re.findall(pattern, content)

        for match in matches:
            color1, color2, result, chance = match
            chance_int = int(chance)

            # Сохраняем все мутации (для графа)
            self.color_graph[color1].append((result, chance_int))
            self.color_graph[color2].append((result, chance_int))
            self.all_colors.update([color1, color2, result])

            mutation = ColorMutation(color1, color2, result, chance_int)
            self.color_mutations.append(mutation)

        # Для каждого цвета оставляем только лучший шанс для каждого результата
        for color in self.color_graph:
            best_chance = {}
            for result, chance in self.color_graph[color]:
                if result not in best_chance or chance > best_chance[result]:
                    best_chance[result] = chance
            self.color_graph[color] = [(r, c) for r, c in best_chance.items()]

        print(f"✅ Загружено цветовых мутаций: {len(self.color_mutations)}")

    def load_base_flowers(self, flowers: List[Tuple[str, str]]):
        """Загружает базовые цветы"""
        for name, color in flowers:
            flower = Flower(name, color)
            self.base_flowers.append(flower)
            self.base_colors.add(color)
            self.flower_by_color[color] = flower

        print(f"✅ Загружено базовых цветов: {len(self.base_flowers)}")

    def load_mutatron_recipes(self, filename: str):
        """Загружает рецепты мутатрона"""
        with open(filename, 'r', encoding='utf-8') as f:
            reader = csv.DictReader(f, delimiter='\t')
            for row in reader:
                recipe = MutatronRecipe(
                    result=row['Результат'].strip(),
                    parent1=row['Родитель 1'].strip(),
                    parent2=row['Родитель 2'].strip(),
                    chance=int(row['Шанс'])
                )
                self.mutatron_recipes.append(recipe)
                self.mutatron_by_result[recipe.result] = recipe

        print(f"✅ Загружено рецептов мутатрона: {len(self.mutatron_recipes)}")

    def find_unique_dyes(self, max_depth=10):
        """Находит все уникальные красители"""
        dye_paths = {}

        # 1. СНАЧАЛА ДОБАВЛЯЕМ ВСЕ ЦВЕТА ИЗ МУТАТРОНА
        # Они имеют selection = 0, steps = количество шагов для получения цветка
        for color in self.mutatron_colors:
            # Находим вид цветка этого цвета
            for species, c in self.species_to_color.items():
                if c == color and species in self.mutatron_by_result:
                    # Получаем минимальные steps для этого цветка
                    flower_steps = self.get_flower_steps(species)
                    dye_paths[color] = (flower_steps, 0, 100, "", "")
                    break
            else:
                # Если не нашли вид, добавляем с steps=1
                dye_paths[color] = (1, 0, 100, "", "")

        # 2. Базовые цвета
        for color in self.base_colors:
            dye_paths[color] = (0, 0, 100, "", "")

        # 3. BFS поиск для уникальных красителей
        available = set(dye_paths.keys())
        queue = deque(list(available))
        processed = set(available)

        # Список исключений
        EXCLUDED_COLORS = self.base_colors | self.mutatron_colors

        while queue:
            current = queue.popleft()
            current_steps, current_selection, _, _, _ = dye_paths[current]

            if current_steps >= max_depth:
                continue

            for mutation in self.color_mutations:
                # Проверяем, является ли current одним из родителей
                if mutation.parent1 == current:
                    other_parent = mutation.parent2
                elif mutation.parent2 == current:
                    other_parent = mutation.parent1
                else:
                    continue

                # Проверяем, доступен ли другой родитель
                if other_parent not in dye_paths:
                    continue

                other_steps, other_selection, _, _, _ = dye_paths[other_parent]
                result = mutation.result

                # Новый шаг - всегда +1 от максимального пути родителей
                new_steps = max(current_steps, other_steps) + 1

                # Selection - максимальный из родителей
                new_selection = max(current_selection, other_selection)

                # Если результат уникальный (не в excluded), добавляем 1
                if result not in EXCLUDED_COLORS:
                    new_selection += 1

                # Проверка: selection не может быть больше steps
                if new_selection > new_steps:
                    new_selection = new_steps

                # Проверяем, лучше ли этот путь
                if result not in dye_paths:
                    dye_paths[result] = (new_steps, new_selection, mutation.chance,
                                         mutation.parent1, mutation.parent2)
                    if result not in processed:
                        processed.add(result)
                        queue.append(result)
                else:
                    old_steps, old_selection, old_chance, _, _ = dye_paths[result]

                    # Пропускаем, если результат в списке исключений
                    if result in EXCLUDED_COLORS:
                        continue

                    # Сравниваем по приоритету
                    if (new_selection < old_selection or
                            (new_selection == old_selection and new_steps < old_steps) or
                            (new_selection == old_selection and new_steps == old_steps and
                             mutation.chance > old_chance)):
                        dye_paths[result] = (new_steps, new_selection, mutation.chance,
                                             mutation.parent1, mutation.parent2)
                        if result not in processed:
                            processed.add(result)
                            queue.append(result)

        # Финальная фильтрация - оставляем только уникальные красители
        unique_dyes = {}
        for color, (steps, selection, chance, p1, p2) in dye_paths.items():
            # Пропускаем базовые и мутатронные цвета
            if color in EXCLUDED_COLORS:
                continue
            # Пропускаем те, что не имеют пути или selection=0
            if steps >= sys.maxsize or selection == 0:
                continue
            unique_dyes[color] = (steps, selection, chance, p1, p2)

        print(f"✅ Найдено уникальных красителей: {len(unique_dyes)}")
        return unique_dyes

    def get_flower_steps(self, species: str, cache=None) -> int:
        """Возвращает минимальное количество шагов для получения цветка"""
        if cache is None:
            cache = {}

        if species in cache:
            return cache[species]

        if species in self.base_species:
            cache[species] = 0
            return 0

        recipe = self.mutatron_by_result.get(species)
        if not recipe:
            cache[species] = 999
            return 999

        parent1_steps = self.get_flower_steps(recipe.parent1, cache)
        parent2_steps = self.get_flower_steps(recipe.parent2, cache)

        steps = max(parent1_steps, parent2_steps) + 1
        cache[species] = steps
        return steps

    def setup_species_mapping(self):
        """Настраивает соответствие видов и русских названий"""
        self.species_to_russian = {
            "DANDELION": "одуванчик", "POPPY": "мак", "ORCHID": "орхидея",
            "ALLIUM": "лук", "BLUET": "голубой василек", "TULIP": "тюльпан",
            "TULIP_WHITE": "тюльпан", "TULIP_CRIMSON": "тюльпан", "TULIP_DARK_ORANGE": "тюльпан",
            "DAISY": "маргаритка", "CORNFLOWER": "василек", "PANSY": "анютины глазки",
            "IRIS": "ирис", "LAVENDER": "лаванда", "VIOLA": "фиалка",
            "DAFFODIL": "нарцисс", "DAHLIA": "георгин", "PEONY": "пеоны",
            "ROSE": "роза", "LILAC": "сирень", "HYDRANGEA": "гортензия",
            "FOXGLOVE": "наперстянка", "ZINNIA": "цинния", "CHRYSANTHEMUM": "хризантема",
            "MARIGOLD": "ноготки", "GERANIUM": "герань", "AZALEA": "азалия",
            "PRIMROSE": "первоцвет", "ASTER": "астра", "CARNATION": "гвоздика садовая",
            "LILY": "лилия", "YARROW": "тысячелистник", "PETUNIA": "петуния",
            "AGAPANTHUS": "агапантус", "FUCHSIA": "фуксия", "DIANTHUS": "гвоздика",
            "FORGET": "незабудка", "ANEMONE": "анемон", "AQUILEGIA": "аквилегия",
            "EDELWEISS": "эдельвейс", "SCABIOUS": "скабиоза", "CONEFLOWER": "эхинация",
            "GAILLARDIA": "гайлардия", "AURICULA": "первоцвет ушковидный",
            "CAMELLIA": "камелия", "GOLDENROD": "золотарник", "ALTHEA": "алтей",
            "PENSTEMON": "пенстемон", "DELPHINIUM": "живокость", "HOLLYHOCK": "штокроза",
        }

        # Обратный маппинг
        for species, russian in self.species_to_russian.items():
            self.russian_by_species[species] = russian
            self.species_by_russian[russian].append(species)

    def setup_flower_color_mapping(self):
        """Сопоставляет виды с цветами"""
        self.species_to_color = {
            "DANDELION": "YELLOW", "POPPY": "RED", "ORCHID": "DEEP_SKY_BLUE",
            "ALLIUM": "MEDIUM_PURPLE", "BLUET": "LAVENDER", "TULIP": "VIOLET",
            "TULIP_WHITE": "WHITE", "TULIP_CRIMSON": "CRIMSON", "TULIP_DARK_ORANGE": "DARK_ORANGE",
            "DAISY": "WHITE", "CORNFLOWER": "SKY_BLUE", "PANSY": "PINK",
            "IRIS": "LIGHT_GRAY", "LAVENDER": "MEDIUM_ORCHID", "VIOLA": "MEDIUM_PURPLE",
            "DAFFODIL": "YELLOW", "DAHLIA": "HOT_PINK", "PEONY": "THISTLE",
            "ROSE": "RED", "LILAC": "PLUM", "HYDRANGEA": "DEEP_SKY_BLUE",
            "FOXGLOVE": "HOT_PINK", "ZINNIA": "MEDIUM_VIOLET_RED", "CHRYSANTHEMUM": "VIOLET",
            "MARIGOLD": "GOLD", "GERANIUM": "HOT_PINK", "AZALEA": "HOT_PINK",
            "PRIMROSE": "RED", "ASTER": "MEDIUM_PURPLE", "CARNATION": "CRIMSON",
            "LILY": "PALE_PINK", "YARROW": "YELLOW", "PETUNIA": "MEDIUM_VIOLET_RED",
            "AGAPANTHUS": "DEEP_SKY_BLUE", "FUCHSIA": "HOT_PINK", "DIANTHUS": "CRIMSON",
            "FORGET": "PALE_BLUE", "ANEMONE": "RED", "AQUILEGIA": "SLATE_BLUE",
            "EDELWEISS": "WHITE", "SCABIOUS": "ROYAL_BLUE", "CONEFLOWER": "VIOLET",
            "GAILLARDIA": "DARK_ORANGE", "AURICULA": "RED", "CAMELLIA": "CRIMSON",
            "GOLDENROD": "GOLD", "ALTHEA": "THISTLE", "PENSTEMON": "MEDIUM_ORCHID",
            "DELPHINIUM": "DARK_SLATE_GRAY", "HOLLYHOCK": "BLACK",
        }

        # Определяем цвета, доступные через мутатрон
        for recipe in self.mutatron_recipes:
            color = self.species_to_color.get(recipe.result)
            if color:
                self.mutatron_colors.add(color)

        # Создаем цветы для всех видов
        for species, color in self.species_to_color.items():
            russian = self.russian_by_species.get(species)
            if russian:
                flower = Flower(russian, color)
                self.flower_by_species[species] = flower
                if color not in self.flower_by_color:
                    self.flower_by_color[color] = flower

        # Добавляем базовые виды
        for flower in self.base_flowers:
            for species, russian in self.species_to_russian.items():
                if russian == flower.name:
                    self.base_species.add(species)
                    self.flower_by_species[species] = flower

    def _get_russian_color(self, color: str) -> str:
        """Возвращает русское название цвета"""
        return self.color_names.get(color, color.lower())

    def _is_color_available(self, color: str) -> bool:
        """Проверяет, доступен ли цвет (базовый или через мутатрон)"""
        return color in self.base_colors or color in self.mutatron_colors

    # ========================================================================
    # ПОИСК ПУТЕЙ ДЛЯ ЦВЕТОВ (КРАСИТЕЛЕЙ)
    # ========================================================================

    def find_best_color_paths(self, max_depth: int = 10):
        """Находит лучшие пути для всех цветов (красителей)"""
        print("🔄 Поиск путей для красителей...")

        # Инициализация
        for color in self.all_colors:
            self.best_color_paths[color] = (sys.maxsize, sys.maxsize, 0, "", "")

        # Базовые цвета
        for color in self.base_colors:
            self.best_color_paths[color] = (0, 0, 100, "", "")

        # Цвета из мутатрона
        for color in self.mutatron_colors:
            self.best_color_paths[color] = (1, 0, 100, "", "")  # steps=1, steps_selection=0

        # Очередь для BFS
        queue = deque()

        # Добавляем все доступные цвета
        for color in self.base_colors | self.mutatron_colors:
            queue.append((color, 1, 0))  # (цвет, steps, steps_selection)

        # Множество обработанных цветов
        processed = set(self.base_colors | self.mutatron_colors)

        while queue:
            current_color, current_steps, current_selection = queue.popleft()

            if current_steps >= max_depth:
                continue

            # Ищем мутации, где current_color является родителем
            for next_color, chance in self.color_graph.get(current_color, []):
                if next_color in processed:
                    # Проверяем, можем ли улучшить
                    best = self.best_color_paths[next_color]
                    new_steps = current_steps + 1

                    # Определяем steps_selection для результата
                    new_selection = current_selection
                    if not self._is_color_available(next_color):
                        new_selection += 1

                    # Проверяем, лучше ли этот путь
                    if (new_selection < best[1] or
                            (new_selection == best[1] and new_steps < best[0]) or
                            (new_selection == best[1] and new_steps == best[0] and chance > best[2])):

                        # Нам нужно знать родителей для этого пути
                        # Ищем мутацию с таким результатом
                        for p1, p2, res, ch in self.color_mutations:
                            if res == next_color and ch == chance:
                                self.best_color_paths[next_color] = (new_steps, new_selection, chance, p1, p2)
                                break

                        if next_color not in processed:
                            processed.add(next_color)
                            queue.append((next_color, new_steps, new_selection))

        # Статистика
        found = sum(1 for v in self.best_color_paths.values() if v[0] < sys.maxsize)
        print(f"✅ Найдено путей для {found} цветов")

    def get_color_path(self, color: str) -> Optional[Tuple[int, int, int, str, str]]:
        """Возвращает лучший путь для цвета"""
        result = self.best_color_paths.get(color)
        if result and result[0] < sys.maxsize:
            return result
        return None

    # ========================================================================
    # ПОИСК ПУТЕЙ ДЛЯ ЦВЕТКОВ
    # ========================================================================

    def find_mutatron_path(self, target_flower: Flower) -> Optional[FlowerPath]:
        """Находит путь через мутатрон"""
        # Ищем вид по цветку
        target_species = None
        for species, flower in self.flower_by_species.items():
            if flower == target_flower:
                target_species = species
                break

        if not target_species:
            return None

        recipe = self.mutatron_by_result.get(target_species)
        if not recipe:
            return None

        parent1 = self.flower_by_species.get(recipe.parent1)
        parent2 = self.flower_by_species.get(recipe.parent2)

        if not parent1 or not parent2:
            return None

        # Получаем пути для родителей (нужно для расчета steps и steps_selection)
        parent1_path = self.get_best_flower_path(parent1)
        parent2_path = self.get_best_flower_path(parent2)

        if not parent1_path or not parent2_path:
            return None

        # steps = max(steps родителей) + 1
        steps = max(parent1_path.steps, parent2_path.steps) + 1

        # steps_selection = max(steps_selection родителей)
        # (потому что сам мутатрон не добавляет селекции)
        steps_selection = max(parent1_path.steps_selection, parent2_path.steps_selection)

        return FlowerPath(
            flower=target_flower,
            steps=steps,
            steps_selection=steps_selection,
            chance=recipe.chance,
            parents=(parent1, parent2),
            recipe_type="mutatron"
        )

    def find_color_flower_path(self, target_flower: Flower) -> Optional[FlowerPath]:
        """Находит путь для цветка через цветовые мутации"""
        target_color = target_flower.color

        # Получаем путь для цвета
        color_path = self.get_color_path(target_color)
        if not color_path:
            return None

        steps, steps_selection, chance, p1_color, p2_color = color_path

        # Если это базовый цвет или мутатронный, не показываем как цветовую мутацию
        if steps == 0 or (steps == 1 and steps_selection == 0):
            return None

        # Находим цветы-родители
        parent1 = self.flower_by_color.get(p1_color)
        parent2 = self.flower_by_color.get(p2_color)

        if not parent1 or not parent2:
            return None

        return FlowerPath(
            flower=target_flower,
            steps=steps,
            steps_selection=steps_selection,
            chance=chance,
            parents=(parent1, parent2),
            recipe_type="color"
        )

    def get_best_flower_path(self, flower: Flower) -> Optional[FlowerPath]:
        """Возвращает лучший путь для цветка"""
        cache_key = f"{flower.name}_{flower.color}"
        if cache_key in self.cache:
            return self.cache[cache_key]

        # Базовые цветы
        if flower in self.base_flowers:
            path = FlowerPath(
                flower=flower,
                steps=0,
                steps_selection=0,
                chance=100,
                recipe_type="base"
            )
            self.cache[cache_key] = path
            return path

        best_path = None

        # Путь через мутатрон
        mutatron_path = self.find_mutatron_path(flower)
        if mutatron_path:
            best_path = mutatron_path

        # Путь через цветовые мутации
        color_path = self.find_color_flower_path(flower)
        if color_path:
            if not best_path or color_path < best_path:
                best_path = color_path

        self.cache[cache_key] = best_path
        return best_path

    def calculate_all_paths(self, all_flowers: List[Flower]) -> Dict[str, FlowerPath]:
        """Рассчитывает пути для всех цветков"""
        # Находим уникальные красители
        unique_dyes = self.find_unique_dyes(max_depth=8)  # <-- ИСПОЛЬЗУЕМ НОВУЮ ФУНКЦИЮ

        results = {}

        # Обрабатываем переданные цветы
        for flower in all_flowers:
            best = self.get_best_flower_path(flower)
            if best:
                results[f"{flower.name}_{flower.color}"] = best

        # Добавляем уникальные красители
        for color, (steps, selection, chance, p1, p2) in unique_dyes.items():  # <-- ИСПОЛЬЗУЕМ unique_dyes
            # Создаем "цветок" для красителя
            russian_name = self._get_russian_color(color)
            flower = Flower(russian_name, color)

            parent1 = self.flower_by_color.get(p1)
            parent2 = self.flower_by_color.get(p2)

            if parent1 and parent2:
                path = FlowerPath(
                    flower=flower,
                    steps=steps,
                    steps_selection=selection,  # <-- selection уже рассчитан правильно
                    chance=chance,
                    parents=(parent1, parent2),
                    recipe_type="color"
                )
                results[f"{flower.name}_{flower.color}"] = path

        return results

    # ========================================================================
    # ЭКСПОРТ
    # ========================================================================

    def export_to_csv(self, filename: str, flower_paths: Dict[str, FlowerPath], dye_paths: Dict):
        """Экспортирует пути в CSV"""
        with open(filename, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(['Тип', 'Название', 'Шагов всего', 'Ручных шагов', 'Родители', 'Шанс'])

            # Цветы из мутатрона
            for path in sorted(flower_paths.values()):
                if path.recipe_type == "mutatron":
                    parents_str = ""
                    if path.parents:
                        parents_str = f"{path.parents[0].name} + {path.parents[1].name}"

                    writer.writerow([
                        'мутатрон',
                        path.flower.name,
                        path.steps,
                        path.steps_selection,
                        parents_str,
                        ''
                    ])

            # Уникальные красители
            dye_list = []
            for color, (steps, selection, chance, p1, p2) in dye_paths.items():
                dye_list.append((color, steps, selection, chance, p1, p2))

            dye_list.sort(key=lambda x: (x[2], x[1], -x[3]))

            for color, steps, selection, chance, p1, p2 in dye_list:
                russian_color = self._get_russian_color(color)
                writer.writerow([
                    'краситель',
                    russian_color,
                    steps,
                    selection,
                    f"{self._get_russian_color(p1)} + {self._get_russian_color(p2)}",
                    f"{chance}%"
                ])

    def export_readme(self, filename: str, flower_paths: Dict[str, FlowerPath], dye_paths: Dict):
        """Экспортирует красивое описание"""
        with open(filename, 'w', encoding='utf-8') as f:
            f.write("# 🌸 Рецепты цветов для Binnie's Botany\n\n")

            # Базовые цветы - просто список всех из BASE_FLOWERS
            f.write("## 🎯 Базовые цветы\n")
            for flower in self.base_flowers:
                russian_color = self._get_russian_color(flower.color)
                f.write(f"- {flower.name} ({russian_color})\n")

            # Цветы из мутатрона
            f.write("\n## ⚡ Цветы из мутатрона\n")
            f.write("_Получаются в мутатроне_\n\n")

            mutatron_list = []
            for path in flower_paths.values():
                if path.recipe_type == "mutatron":
                    mutatron_list.append(path)

            for path in sorted(mutatron_list):
                russian_color = self._get_russian_color(path.flower.color)
                f.write(f"### {path.flower.name} ({russian_color})\n")
                if path.parents:
                    f.write(f"**Родители:** {path.parents[0].name} + {path.parents[1].name}\n")
                f.write("\n")

            # Уникальные красители
            f.write("\n## 🎨 Уникальные красители\n")
            f.write("_Получаются только через цветовые мутации на грядках_\n\n")

            # Преобразуем в список для сортировки
            dye_list = []
            for color, (steps, selection, chance, p1, p2) in dye_paths.items():
                dye_list.append((color, steps, selection, chance, p1, p2))

            # Сортируем: сначала по selection, потом по steps, потом по убыванию chance
            dye_list.sort(key=lambda x: (x[2], x[1], -x[3]))

            for color, steps, selection, chance, p1, p2 in dye_list:
                russian_color = self._get_russian_color(color)
                f.write(f"### {russian_color}\n")
                f.write(f"**Ручных шагов:** {selection}\n")
                f.write(f"**Шагов всего:** {steps}\n")
                f.write(f"**Родители:** {self._get_russian_color(p1)} + {self._get_russian_color(p2)}\n")
                f.write(f"**Шанс:** {chance}%\n\n")

    def export_for_web(self, filename: str, dye_paths: Dict):
        """Экспортирует данные для веб-визуализации"""
        web_data = {}

        for color, (steps, selection, chance, p1, p2) in dye_paths.items():
            russian_name = self._get_russian_color(color)

            # Строим полное дерево для этого цвета
            tree = self.build_color_tree(color, dye_paths)

            web_data[russian_name] = {
                "color": color,
                "steps": steps,
                "selection": selection,
                "chance": chance,
                "parents": [
                    self._get_russian_color(p1),
                    self._get_russian_color(p2)
                ],
                "tree": tree  # рекурсивное дерево
            }

        with open(filename, 'w', encoding='utf-8') as f:
            json.dump(web_data, f, ensure_ascii=False, indent=2)

    def build_color_tree(self, color: str, dye_paths: Dict, visited=None):
        """Строит дерево для цвета"""
        if visited is None:
            visited = set()

        if color in visited:
            return {"name": self._get_russian_color(color), "cycle": True}

        visited.add(color)

        # Проверяем, есть ли цвет в dye_paths
        if color not in dye_paths:
            # Проверяем базовые/мутатронные цвета
            if color in self.base_colors:
                # Найти цветок этого цвета
                for flower in self.base_flowers:
                    if flower.color == color:
                        return {
                            "name": f"{flower.name} ({self._get_russian_color(color)})",
                            "type": "base"
                        }
            elif color in self.mutatron_colors:
                # Найти цветок из мутатрона
                for species, c in self.species_to_color.items():
                    if c == color:
                        flower_name = self.russian_by_species.get(species, species)
                        return {
                            "name": f"{flower_name} ({self._get_russian_color(color)})",
                            "type": "mutatron"
                        }
            return {"name": self._get_russian_color(color), "type": "unknown"}

        steps, selection, chance, p1, p2 = dye_paths[color]

        return {
            "name": self._get_russian_color(color),
            "type": "dye",
            "chance": chance,
            "children": [
                self.build_color_tree(p1, dye_paths, visited.copy()),
                self.build_color_tree(p2, dye_paths, visited.copy())
            ]
        }

# ============================================================================
# ДАННЫЕ
# ============================================================================

BASE_FLOWERS = [
    ("одуванчик", "YELLOW"), ("мак", "RED"), ("орхидея", "DEEP_SKY_BLUE"),
    ("лук", "MEDIUM_PURPLE"), ("голубой василек", "LAVENDER"),
    ("тюльпан", "VIOLET"), ("тюльпан", "WHITE"), ("тюльпан", "CRIMSON"),
    ("тюльпан", "DARK_ORANGE"), ("маргаритка", "WHITE"), ("пеоны", "THISTLE"),
    ("роза", "RED"), ("сирень", "PLUM"),
]

TARGET_FLOWERS = [
    ("василек", "SKY_BLUE"), ("анютины глазки", "PINK"), ("ирис", "LIGHT_GRAY"),
    ("лаванда", "MEDIUM_ORCHID"), ("фиалка", "MEDIUM_PURPLE"), ("нарцисс", "YELLOW"),
    ("георгин", "HOT_PINK"), ("гортензия", "DEEP_SKY_BLUE"), ("наперстянка", "HOT_PINK"),
    ("цинния", "MEDIUM_VIOLET_RED"), ("хризантема", "VIOLET"), ("ноготки", "GOLD"),
    ("герань", "HOT_PINK"), ("азалия", "HOT_PINK"), ("первоцвет", "RED"),
    ("астра", "MEDIUM_PURPLE"), ("гвоздика садовая", "CRIMSON"), ("лилия", "PALE_PINK"),
    ("тысячелистник", "YELLOW"), ("петуния", "MEDIUM_VIOLET_RED"), ("агапантус", "DEEP_SKY_BLUE"),
    ("фуксия", "HOT_PINK"), ("гвоздика", "CRIMSON"), ("незабудка", "PALE_BLUE"),
    ("анемон", "RED"), ("аквилегия", "SLATE_BLUE"), ("эдельвейс", "WHITE"),
    ("скабиоза", "ROYAL_BLUE"), ("эхинация", "VIOLET"), ("гайлардия", "DARK_ORANGE"),
    ("первоцвет ушковидный", "RED"), ("камелия", "CRIMSON"), ("золотарник", "GOLD"),
    ("алтей", "THISTLE"), ("пенстемон", "MEDIUM_ORCHID"), ("живокость", "DARK_SLATE_GRAY"),
    ("штокроза", "BLACK"),
]


# ============================================================================
# ОСНОВНАЯ ФУНКЦИЯ
# ============================================================================

def main():
    parser = BotanyFlowerParser()

    print("🔄 Загрузка данных...")
    parser.setup_species_mapping()
    parser.load_color_mutations("EnumFlowerColor.java")
    parser.load_base_flowers(BASE_FLOWERS)
    parser.load_mutatron_recipes("mutatron_recipes.csv")
    parser.setup_flower_color_mapping()  # ← ПЕРЕМЕСТИТЬ СЮДА, ПОСЛЕ ЗАГРУЗКИ РЕЦЕПТОВ

    target_flowers = [Flower(name, color) for name, color in TARGET_FLOWERS]

    print("\n🔄 Поиск оптимальных путей...")
    flower_paths = parser.calculate_all_paths(target_flowers)
    dye_paths = parser.find_unique_dyes(max_depth=8)

    print("\n💾 Экспорт результатов...")
    parser.export_to_csv("flowers_paths.csv", flower_paths, dye_paths)
    parser.export_readme("FLOWERS_RECIPES.md", flower_paths, dye_paths)
    parser.export_for_web('dye_paths.json', dye_paths)

    print("\n" + "=" * 60)
    print("📊 СТАТИСТИКА")
    print("=" * 60)
    print(f"Базовых цветов: {len(parser.base_flowers)}")
    print(f"Рецептов мутатрона: {len(parser.mutatron_recipes)}")
    print(f"Найдено путей для цветков: {len(flower_paths)}")
    print(f"Найдено уникальных красителей: {len(dye_paths)}")


if __name__ == "__main__":
    main()