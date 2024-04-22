/**
 * @author Rikhav Shah
 * @email atob("cmlraGF2LnNoYWhAYmVya2VsZXkuZWR1")
 */

const activeOfficeRef = {};

function setupBuilding() {
	function makeFloor(floorNum) {
		const wrapper = document.createElement("div");
		wrapper.className = "floor-map";
		const im = new Image();
		im.src = `floor-${floorNum}.png?cachebust=${Date.now()}`;
		wrapper.append(im);
		window.officesByFloor[floorNum].forEach((office) => {
			const { number, capacity, x1, x2, y1, y2 } = office;
			const div = document.createElement("div");

			if (typeof x1 != "object") {
				const [a, b, c, d] = [x1 / 1160, y1 / 730, x2 / 1160, y2 / 730].map(
					(v) => `${v * 100}%`
				);
				div.style.clipPath = `polygon(${a} ${b}, ${c} ${b}, ${c} ${d}, ${a} ${d})`;
			} else {
				div.style.clipPath = `polygon(${x1
					.map((x, i) => `${x / 11.6}% ${y1[i] / 7.3}%`)
					.join(", ")})`;
			}

			div.className = "office-box";

			div.dataset.number = number;
			div.addEventListener("mouseover", scrollToOffice.bind(null, number));
			div.addEventListener("mouseleave", scrollOffOffice.bind(null, number));

			wrapper.append(div);
		});
		return wrapper;
	}

	const floor7 = makeFloor("7");
	const floor8 = makeFloor("8");
	const floor9 = makeFloor("9");
	const floor0 = makeFloor("1");

	window.goToFloor = function (num) {
		if (num == "1") {
			num = "0"
		}
		floor7.dataset.active = "7" == num ? 1 : 0;
		floor8.dataset.active = "8" == num ? 1 : 0;
		floor9.dataset.active = "9" == num ? 1 : 0;
		floor0.dataset.active = "0" == num ? 1 : 0;
		building.dataset.floor = num;
	};

	floor7.dataset.active = 1;
	window.addEventListener("keydown", (evt) => "78901".includes(evt.key) && goToFloor(evt.key));

	building.prepend(floor7);
	building.prepend(floor8);
	building.prepend(floor9);
	building.prepend(floor0);
}

function setupDrawOrder() {
	window.blocks.forEach((block) => {
		const div = document.createElement("div");
		div.className = "block";
		div.dataset.done = block.done ? 1 : 0;
		div.dataset.searchable = block.people.join(", ").toLowerCase();
		const time = block.time == -1 ? "" : (block.time
			? new Date(block.time).toLocaleTimeString([], {
					hour: "numeric",
					minute: "numeric",
			  })
			: "(squat)");
		div.innerHTML = `
            <div>${time} (priority ${block.priority})</div>
            ${block.people.map((person) => `<div>${person}</div>`).join("")}
        `;
		drawOrder.lastElementChild.append(div);
		block.elmt = div;
	});

	setTimeout(
		() =>
			window.blocks.find((block) => !block.done)?.elmt.scrollIntoView({ behavior: "smooth" }),
		200
	);
}

function setupOfficePops() {
	function highlightOfficeBox(number) {
		const old = document.querySelector(`.office-box[data-number="${activeOfficeRef.current}"]`);
		if (old) old.dataset.hover = 0;

		activeOfficeRef.current = number;
		goToFloor(number[0]);
		const el = document.querySelector(`.office-box[data-number="${number}"]`);
		if (!el) return;
		el.dataset.hover = 1;
		setTimeout(() => (el.dataset.hover = 0), 2000);
	}

	function lazyHighlightOfficeBox(number, value) {
		const el = document.querySelector(`.office-box[data-number="${number}"]`);
		if (el) el.dataset.hover = value;
	}

	window.officePops.forEach(({ number, people }) => {
		const div = document.createElement("div");
		div.className = "block";
		div.dataset.searchable = people.join(", ").toLowerCase();
		div.dataset.number = number;
		div.onclick = highlightOfficeBox.bind(null, div.dataset.number);

		div.onmouseenter = lazyHighlightOfficeBox.bind(null, div.dataset.number, 1);
		div.onmouseleave = lazyHighlightOfficeBox.bind(null, div.dataset.number, 0);

		div.innerHTML = `
        <div>Office ${number} (${people.length} / ${window.officesByNumber[number].capacity})</div>
        ${people.map((person) => `<div>${person}</div>`).join("")}
        `;
		setOffices.lastElementChild.append(div);
	});
}

function switchToMobileLayout() {
	const drawOrder_ = drawOrder;
	const setOffices_ = setOffices;
	root.removeChild(drawOrder_);
	root.removeChild(setOffices_);
	const row2 = document.createElement('div');
	row2.id = 'row2';
	row2.append(drawOrder_);
	row2.append(setOffices_);
	root.append(row2);
}

function scrollToOffice(number) {
	Array.from(setOffices.lastElementChild.children).forEach((child) => {
		child.dataset.active = child.dataset.number == number ? 1 : 0;
		if (child.dataset.number == number) {
			child.scrollIntoView({ behavior: "smooth" });
		}
	});
}

function scrollOffOffice(number) {
	Array.from(setOffices.lastElementChild.children).find((child) => {
		if (child.dataset.number == number) child.dataset.active = 0;
	});
}

function searchForName(el, text) {
	text = text.toLowerCase();
	Array.from(el.children).forEach((child) => {
		child.style.display = child.dataset.searchable.includes(text) ? "block" : "none";
	});
}

function go() {
	window.searchForNameInDraw = searchForName.bind(null, drawOrder.lastElementChild);
	window.searchForNameInOffice = searchForName.bind(null, setOffices.lastElementChild);

	setupBuilding();
	setupDrawOrder();
	setupOfficePops();
	if(window.innerHeight > window.innerWidth)
		switchToMobileLayout();
}
