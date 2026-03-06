let dyeData = {};

// Загрузка данных
fetch('dye_paths.json')
    .then(response => response.json())
    .then(data => {
        dyeData = data;
        populateDatalist();
    });

function populateDatalist() {
    const datalist = document.getElementById('dye-list');
    for (let name in dyeData) {
        let option = document.createElement('option');
        option.value = name;
        datalist.appendChild(option);
    }
}

function searchDye() {
    const query = document.getElementById('search').value;
    if (dyeData[query]) {
        drawTree(dyeData[query].tree);
        showInfo(dyeData[query]);
    } else {
        alert('Краситель не найден');
    }
}

function drawTree(treeData) {
    const width = 800;
    const height = 600;

    // Очищаем контейнер
    d3.select("#tree-container").html("");

    const svg = d3.select("#tree-container")
        .append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(40,40)");

    const root = d3.hierarchy(treeData);
    const treeLayout = d3.tree().size([height-80, width-160]);
    treeLayout(root);

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
        .attr("stroke", "#555")
        .attr("stroke-width", 2);

    // Рисуем узлы
    const node = svg.selectAll(".node")
        .data(root.descendants())
        .enter()
        .append("g")
        .attr("class", "node")
        .attr("transform", d => `translate(${d.y},${d.x})`);

    node.append("circle")
        .attr("r", 10)
        .attr("fill", d => {
            if (d.data.type === "base") return "#4CAF50";
            if (d.data.type === "mutatron") return "#FF9800";
            return "#2196F3";
        });

    node.append("text")
        .attr("dy", -15)
        .attr("text-anchor", "middle")
        .text(d => d.data.name)
        .style("font-size", "12px");
}

function showInfo(data) {
    const info = document.getElementById('info');
    info.innerHTML = `
        <h3>Информация</h3>
        <p>Шагов всего: ${data.steps}</p>
        <p>Ручных шагов: ${data.selection}</p>
        <p>Шанс последнего скрещивания: ${data.chance}%</p>
    `;
}