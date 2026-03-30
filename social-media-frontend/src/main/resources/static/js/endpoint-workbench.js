(function () {
  function hasInputFields(container) {
    return !!container.querySelector("input, select, textarea");
  }

  function createStyle() {
    var style = document.createElement("style");
    style.textContent = [
      ".endpoint-panel { display: none; margin-top: 12px; }",
      ".endpoint-panel.open { display: block; }",
      ".endpoint-trigger { width: 100%; margin-bottom: 10px; }",
      ".box-header { justify-content: space-between; }",
      ".endpoint-hint { font-size: 12px; color: #64748b; margin-top: 4px; }"
    ].join("\n");
    document.head.appendChild(style);
  }

  function runDirectAction(box, panel) {
    var directLink = panel.querySelector("a[href]");
    if (directLink) {
      window.location.href = directLink.getAttribute("href");
      return;
    }

    var form = panel.querySelector("form");
    if (form) {
      form.submit();
    }
  }

  function setupBox(box) {
    var separator = box.querySelector(".box-sep");
    var header = box.querySelector(".box-header");
    var anchorPoint = separator || header;
    if (!anchorPoint) {
      return;
    }

    var forms = Array.prototype.slice.call(box.querySelectorAll("form"));
    var links = Array.prototype.slice.call(box.querySelectorAll("a"));

    if (forms.length === 0 && links.length === 0) {
      return;
    }

    var panel = document.createElement("div");
    panel.className = "endpoint-panel";

    links.forEach(function (link) {
      panel.appendChild(link);
    });

    forms.forEach(function (form) {
      panel.appendChild(form);
    });

    var trigger = document.createElement("button");
    trigger.type = "button";
    trigger.className = "endpoint-trigger btn-get";

    var requiresFormInput = hasInputFields(panel);
    trigger.textContent = requiresFormInput ? "Open Endpoint" : "Run Endpoint";

    if (!requiresFormInput) {
      var hint = document.createElement("div");
      hint.className = "endpoint-hint";
      hint.textContent = "No input required. Click to fetch results directly.";
      anchorPoint.insertAdjacentElement("afterend", hint);
    }

    trigger.addEventListener("click", function () {
      if (!requiresFormInput) {
        runDirectAction(box, panel);
        return;
      }

      var isOpen = panel.classList.contains("open");
      document.querySelectorAll(".endpoint-panel.open").forEach(function (openPanel) {
        openPanel.classList.remove("open");
      });
      panel.classList.toggle("open", !isOpen);
    });

    anchorPoint.insertAdjacentElement("afterend", trigger);
    trigger.insertAdjacentElement("afterend", panel);
  }

  document.addEventListener("DOMContentLoaded", function () {
    var boxes = document.querySelectorAll(".grid .box");
    if (!boxes.length) {
      return;
    }

    createStyle();
    boxes.forEach(setupBox);
  });
})();
