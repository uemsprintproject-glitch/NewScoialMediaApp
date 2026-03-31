(function () {
  function normalize(value) {
    return (value || "").toString().trim();
  }

  function asComparable(value) {
    var raw = normalize(value).replace(/,/g, "");
    var number = Number(raw);
    if (!Number.isNaN(number) && raw !== "") {
      return { type: "number", value: number };
    }
    return { type: "string", value: raw.toLowerCase() };
  }

  function createControls(table, headers) {
    var controls = document.createElement("div");
    controls.className = "table-tools";

    var columnSelect = document.createElement("select");
    columnSelect.className = "table-filter-column";

    var allOption = document.createElement("option");
    allOption.value = "all";
    allOption.textContent = "All columns";
    columnSelect.appendChild(allOption);

    headers.forEach(function (header, index) {
      var option = document.createElement("option");
      option.value = String(index);
      option.textContent = header.textContent.trim() || "Column " + (index + 1);
      columnSelect.appendChild(option);
    });

    var input = document.createElement("input");
    input.type = "text";
    input.className = "table-filter-input";
    input.placeholder = "Filter rows...";

    var clearBtn = document.createElement("button");
    clearBtn.type = "button";
    clearBtn.className = "table-filter-clear";
    clearBtn.textContent = "Clear";

    controls.appendChild(columnSelect);
    controls.appendChild(input);
    controls.appendChild(clearBtn);

    return { controls: controls, columnSelect: columnSelect, input: input, clearBtn: clearBtn };
  }

  function addStyles() {
    var style = document.createElement("style");
    style.textContent = [
      ".table-tools { display: flex; gap: 8px; align-items: center; padding: 14px 14px 0; flex-wrap: wrap; }",
      ".table-filter-column, .table-filter-input { border: 1px solid #d8dde8; border-radius: 8px; padding: 8px 10px; font-size: 13px; }",
      ".table-filter-input { min-width: 220px; flex: 1; }",
      ".table-filter-clear { border: 1px solid #bfdbfe; background: #eff6ff; color: #1d4ed8; border-radius: 8px; padding: 8px 12px; font-size: 13px; cursor: pointer; }",
      "th.sortable { cursor: pointer; user-select: none; }",
      "th.sortable::after { content: '  ↕'; color: #94a3b8; font-size: 11px; }",
      "th.sorted-asc::after { content: '  ↑'; color: #2563eb; }",
      "th.sorted-desc::after { content: '  ↓'; color: #2563eb; }"
    ].join("\n");
    document.head.appendChild(style);
  }

  function setupTable(table) {
    var headRow = table.querySelector("thead tr");
    var tbody = table.querySelector("tbody");
    if (!headRow || !tbody) {
      return;
    }

    var headers = Array.prototype.slice.call(headRow.querySelectorAll("th"));
    var rows = Array.prototype.slice.call(tbody.querySelectorAll("tr")).filter(function (row) {
      return !row.classList.contains("empty-row") && !row.querySelector(".empty");
    });

    if (!headers.length || !rows.length) {
      return;
    }

    headers.forEach(function (th) {
      th.classList.add("sortable");
    });

    var controls = createControls(table, headers);
    var tableContainer = table.closest(".table-card") || table.parentElement;
    tableContainer.insertBefore(controls.controls, table);

    var currentSort = { index: -1, direction: "asc" };

    function applyFilter() {
      var filterText = normalize(controls.input.value).toLowerCase();
      var selectedColumn = controls.columnSelect.value;

      rows.forEach(function (row) {
        var cells = Array.prototype.slice.call(row.querySelectorAll("td"));
        var visible = true;

        if (filterText) {
          if (selectedColumn === "all") {
            visible = cells.some(function (cell) {
              return normalize(cell.textContent).toLowerCase().includes(filterText);
            });
          } else {
            var idx = Number(selectedColumn);
            visible = cells[idx] && normalize(cells[idx].textContent).toLowerCase().includes(filterText);
          }
        }

        row.style.display = visible ? "" : "none";
      });
    }

    function sortByColumn(index) {
      if (currentSort.index === index) {
        currentSort.direction = currentSort.direction === "asc" ? "desc" : "asc";
      } else {
        currentSort.index = index;
        currentSort.direction = "asc";
      }

      headers.forEach(function (header) {
        header.classList.remove("sorted-asc", "sorted-desc");
      });
      headers[index].classList.add(currentSort.direction === "asc" ? "sorted-asc" : "sorted-desc");

      rows.sort(function (rowA, rowB) {
        var aCell = rowA.querySelectorAll("td")[index];
        var bCell = rowB.querySelectorAll("td")[index];
        var a = asComparable(aCell ? aCell.textContent : "");
        var b = asComparable(bCell ? bCell.textContent : "");

        var compare = 0;
        if (a.type === "number" && b.type === "number") {
          compare = a.value - b.value;
        } else {
          compare = String(a.value).localeCompare(String(b.value));
        }

        return currentSort.direction === "asc" ? compare : -compare;
      });

      rows.forEach(function (row) {
        tbody.appendChild(row);
      });

      applyFilter();
    }

    headers.forEach(function (header, index) {
      header.addEventListener("click", function () {
        sortByColumn(index);
      });
    });

    controls.input.addEventListener("input", applyFilter);
    controls.columnSelect.addEventListener("change", applyFilter);
    controls.clearBtn.addEventListener("click", function () {
      controls.input.value = "";
      controls.columnSelect.value = "all";
      applyFilter();
    });
  }

  document.addEventListener("DOMContentLoaded", function () {
    var tables = document.querySelectorAll("table");
    if (!tables.length) {
      return;
    }

    addStyles();
    tables.forEach(setupTable);
  });
})();
