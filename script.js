let allData = null;

function loadData() {
    fetch('dye_paths.json')
        .then(response => response.json())
        .then(data => {
            allData = data;
            updateDatalist();
        })
        .catch(error => console.error('Ошибка загрузки данных:', error));
}

// Обновляем список при вводе
document.getElementById('search').addEventListener('input', function(e) {
    updateDatalist();
});

// Обработчик выбора из выпадающего списка
document.getElementById('search').addEventListener('change', function(e) {
    const selectedValue = e.target.value;
    if (selectedValue) {
        searchAndDrawWithExactMatch(selectedValue);
        // Не очищаем datalist, он остаётся доступным
    }
});


function searchAndDrawWithExactMatch(exactName) {
    document.getElementById('tree-container').innerHTML = '';

    // Ищем точное совпадение в базовых
    const baseItem = (allData.base_colors || []).find(item =>
        `${item.name} (${item.rgb})` === exactName
    );
    if (baseItem) {
        showBaseResult(baseItem);
        return;
    }

    // Ищем точное совпадение в мутатронных
    const mutatronItem = (allData.mutatron_colors || []).find(item =>
        `${item.name} (${item.rgb})` === exactName
    );
    if (mutatronItem) {
        drawSingleTree(mutatronItem.tree, mutatronItem.name, 'mutatron');
        return;
    }

    // Ищем в уникальных
    const uniqueItem = (allData.unique_dyes || []).find(item =>
        item.name === exactName
    );
    if (uniqueItem) {
        drawSingleTree(uniqueItem.tree, uniqueItem.name, 'dye');
        return;
    }

    showNotFound();
}

function updateDatalist() {
    const category = document.getElementById('category-select').value;
    const searchInput = document.getElementById('search').value.trim().toLowerCase();
    const datalist = document.getElementById('dye-list');
    datalist.innerHTML = '';

    if (!allData) return;

    const addOptions = (items, showColor = true) => {
        items.forEach(item => {
            const value = showColor ? `${item.name} (${item.rgb || item.color})` : item.name;
            const valueLower = value.toLowerCase();

            // Непрерывная последовательность (includes)
            if (valueLower.includes(searchInput)) {
                const option = document.createElement('option');
                option.value = value;
                datalist.appendChild(option);
            }
        });
    };

    if (category === 'base') {
        addOptions(allData.base_colors || [], true);
    }
    else if (category === 'mutatron') {
        addOptions(allData.mutatron_colors || [], true);
    }
    else if (category === 'unique') {
        addOptions(allData.unique_dyes || [], false);
    }
    else {
        addOptions(allData.base_colors || [], true);
        addOptions(allData.mutatron_colors || [], true);
        addOptions(allData.unique_dyes || [], false);
    }
}

// Убираем все лишние обработчики
document.getElementById('search').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        searchAndDraw();
    }
});

document.getElementById('search-btn').addEventListener('click', () => {
    searchAndDraw();
});

function searchAndDraw() {
    const category = document.getElementById('category-select').value;
    const searchTerm = document.getElementById('search').value.trim();

    if (!searchTerm) {
        document.getElementById('tree-container').innerHTML = '<div class="no-data">🔍 Введите название для поиска</div>';
        return;
    }

    if (!allData) return;

    document.getElementById('tree-container').innerHTML = '';
    let found = false;

    // ПОЛНОЕ СОВПАДЕНИЕ при нажатии Enter/кнопки
    const cleanSearchTerm = searchTerm.toLowerCase();

    if (category === 'base') {
        const items = (allData.base_colors || []).filter(item => {
            const displayName = baseFlowerDisplay[item.name] || item.name;
            // Точное совпадение с отображаемым именем или цветом
            return displayName.toLowerCase() === cleanSearchTerm ||
                   item.rgb?.toLowerCase() === cleanSearchTerm;
        });
        items.forEach(item => showBaseResult(item));
        found = items.length > 0;
    }
    else if (category === 'mutatron') {
        const items = (allData.mutatron_colors || []).filter(item =>
            item.name.toLowerCase() === cleanSearchTerm ||
            item.rgb?.toLowerCase() === cleanSearchTerm
        );
        items.forEach(item => drawSingleTree(item.tree, item.name, 'mutatron'));
        found = items.length > 0;
    }
    else if (category === 'unique') {
        const items = (allData.unique_dyes || []).filter(item =>
            item.name.toLowerCase() === cleanSearchTerm
        );
        items.forEach(item => drawSingleTree(item.tree, item.name, 'dye'));
        found = items.length > 0;
    }
    else {
        // Все категории
        const baseItems = (allData.base_colors || []).filter(item => {
            const displayName = baseFlowerDisplay[item.name] || item.name;
            return displayName.toLowerCase() === cleanSearchTerm ||
                   item.rgb?.toLowerCase() === cleanSearchTerm;
        });
        baseItems.forEach(item => showBaseResult(item));

        const mutatronItems = (allData.mutatron_colors || []).filter(item =>
            item.name.toLowerCase() === cleanSearchTerm ||
            item.rgb?.toLowerCase() === cleanSearchTerm
        );
        mutatronItems.forEach(item => drawSingleTree(item.tree, item.name, 'mutatron'));

        const uniqueItems = (allData.unique_dyes || []).filter(item =>
            item.name.toLowerCase() === cleanSearchTerm
        );
        uniqueItems.forEach(item => drawSingleTree(item.tree, item.name, 'dye'));

        found = baseItems.length + mutatronItems.length + uniqueItems.length > 0;
    }

    if (!found) showNotFound();
}


// Обновляем список при изменении категории
document.getElementById('category-select').addEventListener('change', () => {
    updateDatalist();
    document.getElementById('tree-container').innerHTML = '<div class="no-data">🔍 Введите название для поиска</div>';
});

// Слушатель для ввода в поле поиска
document.getElementById('search').addEventListener('input', function(e) {
    const value = e.target.value;
    // Если выбран элемент из datalist (содержит скобки)
    if (value.includes('(') && value.includes(')')) {
        searchAndDrawWithExactMatch(value);
    }
});

document.getElementById('search').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        searchAndDraw();
    }
});

document.getElementById('search-btn').addEventListener('click', () => {
    searchAndDraw();
});

function clearSearch() {
    document.getElementById('search').value = '';
    document.getElementById('tree-container').innerHTML = '<div class="no-data">🔍 Введите название для поиска</div>';

    // Очищаем datalist
    const datalist = document.getElementById('dye-list');
    datalist.innerHTML = '';

    // Принудительно обновляем datalist с полным списком для текущей категории
    updateDatalist();
}

// Исправленный обработчик input
document.getElementById('search').addEventListener('input', function(e) {
    const value = e.target.value;

    // Если поле пустое, показываем полный список
    if (value.trim() === '') {
        updateDatalist(); // updateDatalist сам покажет полный список, т.к. searchInput будет пустым
        return;
    }

    // Если выбран элемент из datalist (содержит скобки)
    if (value.includes('(') && value.includes(')')) {
        searchAndDrawWithExactMatch(value);
    } else {
        // При вводе текста обновляем выпадающий список
        updateDatalist();
    }
});

const baseFlowerDisplay = {
    "одуванчик": "одуванчик",
    "мак": "мак",
    "орхидея": "синяя орхидея",
    "лук": "лук",
    "голубой василек": "голубой василек",
    "тюльпан розовый": "розовый тюльпан",
    "тюльпан белый": "белый тюльпан",
    "тюльпан красный": "красный тюльпан",
    "тюльпан оранжевый": "оранжевый тюльпан",
    "маргаритка": "ромашка",
    "пеоны": "пион",
    "роза": "розовый куст",
    "сирень": "сирень"
};

function showBaseResult(item) {
    const container = document.getElementById('tree-container');
    container.innerHTML = '';

    const displayName = baseFlowerDisplay[item.name] || item.name;

    const card = document.createElement('div');
    card.className = 'base-result-card';
    card.innerHTML = `
        <h3>${displayName}</h3>
        <div class="base-details">
            <div><span class="label">Цвет:</span> ${item.rgb}</div>
            <div><span class="label">Цветок:</span> ${displayName}</div>
        </div>
        <div class="base-note">🌱 Базовый цветок - не требует скрещивания</div>
    `;

    container.appendChild(card);
}

function drawSingleTree(treeData, title, type) {
    const container = document.getElementById('tree-container');
    container.innerHTML = '';

    const wrapper = document.createElement('div');
    wrapper.className = 'tree-wrapper';

    const titleElement = document.createElement('h3');
    titleElement.textContent = title;
    titleElement.className = `title-${type}`;
    wrapper.appendChild(titleElement);

    const svgContainer = document.createElement('div');
    svgContainer.className = 'tree-svg';
    wrapper.appendChild(svgContainer);

    container.appendChild(wrapper);

    setTimeout(() => {
        drawTreeInContainer(treeData, svgContainer, type);
    }, 50);
}

function drawTreeInContainer(treeData, container, type) {
    function calculateTreeDimensions(root) {
        // Сначала вычисляем количество листьев и глубину
        let maxDepth = 0;
        let leafCount = 0;
        let maxWidth = 0;

        // Словарь для подсчета количества узлов на каждом уровне
        const nodesPerLevel = {};

        function traverse(node, depth) {
            maxDepth = Math.max(maxDepth, depth);

            // Считаем узлы на каждом уровне
            nodesPerLevel[depth] = (nodesPerLevel[depth] || 0) + 1;

            if (!node.parents || node.parents.length === 0) {
                leafCount++;
            } else {
                node.parents.forEach(child => traverse(child, depth + 1));
            }
        }

        traverse(treeData, 0);

        // Находим максимальную ширину уровня (максимальное количество узлов на одном уровне)
        maxWidth = Math.max(...Object.values(nodesPerLevel));

        // Рассчитываем размеры
        const minNodeWidth = 300; // Минимальная ширина на узел по горизонтали
        const minNodeHeight = 190; // Минимальная высота на уровень по вертикали

        // Ширина зависит от максимального количества узлов на одном уровне
        const calculatedWidth = Math.max(maxWidth * minNodeWidth, 1200);
        // Высота зависит от глубины дерева
        const calculatedHeight = Math.max((maxDepth + 1) * minNodeHeight, 800);

        return {
            width: Math.min(calculatedWidth, 3500),
            height: Math.min(calculatedHeight, 2500),
            maxDepth: maxDepth,
            maxWidth: maxWidth
        };
    }

    const dimensions = calculateTreeDimensions(treeData);
    const width = Math.min(container.clientWidth || 1400, dimensions.width);
    const height = dimensions.height;

    container.innerHTML = '';
    container.style.overflow = 'auto';
    container.style.minHeight = '600px';
    container.style.maxHeight = '2500px';

    function convertToChildren(node) {
        if (!node) return null;

        let displayName = node.name;
        let nodeType = node.type;

        if (nodeType === 'unknown') {
            nodeType = 'base';
        }

        if (nodeType === 'mutatron') {
            displayName = `${node.name}`;
        } else if (nodeType === 'base') {
            if (node.color) {
                displayName = `${node.name} (${node.color})`;
            }
        } else if (nodeType === 'dye') {
            displayName = `${node.name}`;
        }

        const newNode = {
            name: displayName,
            originalName: node.name,
            color: node.color,
            type: nodeType,
            chance: node.chance
        };

        if (node.parents && Array.isArray(node.parents)) {
            const children = node.parents
                .map(parent => convertToChildren(parent))
                .filter(p => p !== null);

            if (children.length > 0) {
                newNode.children = children;
            }
        }

        return newNode;
    }

    const convertedTree = convertToChildren(treeData);
    if (!convertedTree) return;

    const root = d3.hierarchy(convertedTree);

    // Создаем layout с правильными размерами
    const treeLayout = d3.tree()
        .size([height - 200, width - 400])
        .separation((a, b) => {
            if (a.parent === b.parent) {
                return 2.5;
            }
            return 4;
        });

    treeLayout(root);

    // Добавляем отступы
    root.each(d => {
        d.x = d.x + 100; // Отступ сверху
        d.y = d.y + 200; // Отступ слева
    });

    // === НОВЫЙ КОД: Постобработка для разрешения конфликтов ===
    const MIN_VERTICAL_DISTANCE = 60; // Минимальное расстояние между узлами по вертикали

    // Функция для поиска конфликтов на всех уровнях
    function resolveConflicts() {
        let hasConflicts = true;
        let iterations = 0;
        const maxIterations = 30; // Предотвращаем бесконечный цикл

        while (hasConflicts && iterations < maxIterations) {
            hasConflicts = false;

            // Собираем все узлы
            const allNodes = root.descendants();

            // Проверяем каждую пару узлов
            for (let i = 0; i < allNodes.length; i++) {
                for (let j = i + 1; j < allNodes.length; j++) {
                    const node1 = allNodes[i];
                    const node2 = allNodes[j];

                    // Проверяем только узлы на разных уровнях (разная глубина)
                    if (node1.depth !== node2.depth) {
                        // Проверяем расстояние по вертикали
                        const verticalDistance = Math.abs(node1.x - node2.x);

                        // Если узлы слишком близко по вертикали
                        if (verticalDistance < MIN_VERTICAL_DISTANCE) {
                            // Проверяем, перекрываются ли они по горизонтали
                            const horizontalOverlap = Math.abs(node1.y - node2.y) < 100; // Примерная ширина узла с подписью

                            if (horizontalOverlap) {
                                hasConflicts = true;

                                // Раздвигаем узлы вертикально
                                if (node1.x < node2.x) {
                                    // Сдвигаем нижний узел вниз
                                    const shift = MIN_VERTICAL_DISTANCE - verticalDistance + 10;
                                    shiftSubtree(node2, 'x', shift);
                                } else {
                                    // Сдвигаем нижний узел вниз
                                    const shift = MIN_VERTICAL_DISTANCE - verticalDistance + 10;
                                    shiftSubtree(node1, 'x', shift);
                                }
                            }
                        }
                    }
                }
            }

            iterations++;
        }
    }

    // Функция для сдвига поддерева
    function shiftSubtree(node, axis, shift) {
        node[axis] += shift;

        if (node.children) {
            node.children.forEach(child => shiftSubtree(child, axis, shift));
        }
    }

    // Запускаем разрешение конфликтов
    resolveConflicts();

    // Создаем SVG
    const svg = d3.select(container)
        .append("svg")
        .attr("width", width)
        .attr("height", height + 100)
        .attr("viewBox", `0 0 ${width} ${height + 100}`)
        .attr("preserveAspectRatio", "xMidYMid meet")
        .append("g");

    // Рисуем связи
    svg.selectAll(".link")
        .data(root.links())
        .enter()
        .append("path")
        .attr("class", "link")
        .attr("d", d3.linkHorizontal()
            .x(d => d.y)
            .y(d => d.x))
        .attr("fill", "none")
        .attr("stroke", "#666")
        .attr("stroke-width", 2)
        .attr("stroke-opacity", 0.5);

    // Добавляем узлы
    const node = svg.selectAll(".node")
        .data(root.descendants())
        .enter()
        .append("g")
        .attr("class", "node")
        .attr("transform", d => `translate(${d.y},${d.x})`);

    // Рисуем круги узлов
    node.append("circle")
        .attr("r", d => {
            if (d.depth === 0) return 24;
            if (d.children) return 20;
            return 18;
        })
        .attr("fill", d => {
            if (d.data.type === "base") return "#4CAF50";
            if (d.data.type === "mutatron") return "#FF9800";
            if (d.data.type === "dye") return "#2196F3";
            return "#9E9E9E";
        })
        .attr("stroke", "#222")
        .attr("stroke-width", 2.5);

    // Добавляем текст с названием
    node.append("text")
        .attr("dy", -30)
        .attr("text-anchor", "middle")
        .text(d => {
            let text = d.data.name;
            if (d.data.chance && d.data.type === 'dye') {
                text += ` (${d.data.chance}%)`;
            }
            return text;
        })
        .style("font-size", d => d.depth === 0 ? "16px" : "14px")
        .style("font-weight", "bold")
        .style("fill", "#000")
        .style("text-shadow", "1px 1px 2px white")
        .style("background-color", "rgba(255,255,255,0.8)")
        .style("padding", "2px 4px")
        .style("border-radius", "4px")
        .style("white-space", "nowrap");

    // Добавляем иконки
    node.append("text")
        .attr("dy", 40)
        .attr("text-anchor", "middle")
        .text(d => {
            if (d.data.type === "base") return "🌱";
            if (d.data.type === "mutatron") return "⚡";
            if (d.data.type === "dye") return "🎨";
            return "";
        })
        .style("font-size", "20px")
        .style("font-weight", "bold")
        .style("fill", "#666");

    // Добавляем подпись типа для корневого узла
    if (type === 'mutatron' || type === 'dye') {
        node.filter(d => d.depth === 0)
            .append("text")
            .attr("dy", 70)
            .attr("text-anchor", "middle")
            .text(type === 'mutatron' ? "⚡ Мутатрон" : "🎨 Краситель")
            .style("font-size", "14px")
            .style("font-weight", "bold")
            .style("fill", "#666")
            .style("text-shadow", "1px 1px 2px white");
    }

    // Добавляем обработчик для изменения размера окна
    window.addEventListener('resize', () => {
        const newWidth = container.clientWidth;
        if (Math.abs(newWidth - width) > 100) {
            drawTreeInContainer(treeData, container, type);
        }
    }, { once: true });
}

function showNotFound() {
    const container = document.getElementById('tree-container');
    container.innerHTML = '<div class="no-data">❌ Ничего не найдено</div>';
}

window.onload = loadData;