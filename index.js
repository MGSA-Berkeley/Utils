/**
 * @author Rikhav Shah
 * @email atob("cmlraGF2LnNoYWhAYmVya2VsZXkuZWR1")
 */
const ns = "http://www.w3.org/2000/svg";
const activeOfficeRef = {};

function capitalize(s) {
	return s[0].toUpperCase() + s.substring(1);
}

function ave(arr) {
	let tot = 0;
	for (let i = 0; i < arr.length; i++) tot += arr[i];
	return tot / arr.length;
}

async function setupMap(floorplan, offices, populations) {
	const evans = await fetch('data/' + floorplan).then(x => x.json());

	const floors = Object.assign({}, ...evans.floors.map(({ number, map }) => {
		const g = document.createElementNS(ns, "g");
		g.setAttribute("data-floor", number);
		svgmap.append(g);
		return { [number]: { g, map } };
	}));

	evans.offices.forEach(office => {
		const { number, floor, capacity, xpoints, ypoints } = office;
		if (!offices.includes(number))
			return;

		const points = xpoints.map((x, i) => `${x},${ypoints[i]}`).join(' ');
		const poly = document.createElementNS(ns, "polygon");
		poly.setAttribute("points", points);
		poly.setAttribute('class', 'office');

		const num = document.createElementNS(ns, "text");
		num.innerHTML = "#" + number;
		num.setAttribute("x", Math.min(...xpoints) + 2);
		num.setAttribute("y", Math.max(...ypoints) - 4);
		num.setAttribute('class', 'num');

		const cap = document.createElementNS(ns, "text");
		cap.innerHTML = populations[number] + " / " + capacity;

		cap.setAttribute("x", ave(xpoints));
		cap.setAttribute("y", ave(ypoints) - 7);
		cap.setAttribute('class', 'cap');

		floors[floor].g.append(poly);
		floors[floor].g.append(num);
		floors[floor].g.append(cap);
	});


	function resize() {
		const r = imgmap.height / imgmap.naturalHeight;
		Object.values(floors).map(({ g }) => {
			g.setAttribute("transform", `scale(${r})`);
		});
	}

	function goToFloor(num) {
		if (num == "1" || num == "0") num = "10";
		imgmap.src = 'data/' + capitalize(floors[num].map);
		building.dataset.floor = num;
	};

	goToFloor(10);

	window.onresize = resize;
	imgmap.onload = resize;
	setTimeout(resize, 200);

	window.addEventListener("keydown", (evt) => "78901".includes(evt.key) && goToFloor(evt.key));
}

async function foo() {

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

async function go() {
	const urlParams = new URLSearchParams(window.location.search);

	const year = urlParams.get('year') || new Date().getFullYear();

	const data = await fetch('data/data.json').then(res => res.json()).then(x => x.data.find(y => y.year == year) || x.data[x.data.length - 1]);

	const offices = await fetch('data/' + data.activeoffices).then(res => res.json()).then(x => x.offices);

	const people = await fetch('data/' + data.people).then(res => res.json()).then(x => x.people);
	const populations = {};
	people.forEach(person => {
		if (!(person.office in populations))
			populations[person.office] = 0;
		populations[person.office] += 1;
	})

	setupMap(data.floorplan, offices, populations);

	return;
	window.searchForNameInDraw = searchForName.bind(null, drawOrder.lastElementChild);
	window.searchForNameInOffice = searchForName.bind(null, setOffices.lastElementChild);

	setupBuilding();
	setupDrawOrder();
	setupOfficePops();
	if (window.innerHeight > window.innerWidth)
		switchToMobileLayout();
}
