/**
 * @author Rikhav Shah
 * @email atob("cmlraGF2LnNoYWhAYmVya2VsZXkuZWR1")
 */
const ns = "http://www.w3.org/2000/svg";

function capitalize(s) {
	return s[0].toUpperCase() + s.substring(1);
}

function ave(arr) {
	let tot = 0;
	for (let i = 0; i < arr.length; i++) tot += arr[i];
	return tot / arr.length;
}

function floorDataToObject(floors) {
	return Object.assign({}, ...floors.map(({ number, map }) => {
		const g = document.createElementNS(ns, "g");
		g.setAttribute("data-floor", number);
		svgmap.append(g);
		return { [number]: { g, map } };
	}));
}

function createOfficeSvgElements(office) {
	const { number, capacity, xpoints, ypoints } = office;
	const points = xpoints.map((x, i) => `${x},${ypoints[i]}`).join(' ');
	const poly = document.createElementNS(ns, "polygon");
	poly.setAttribute("points", points);
	poly.setAttribute('class', 'office');
	poly.setAttribute('data-number', number);
	poly.onclick = highlightOfficeInList.bind(null, number);

	const num = document.createElementNS(ns, "text");
	num.innerHTML = "#" + number;
	num.setAttribute("x", Math.min(...xpoints) + 2);
	num.setAttribute("y", Math.max(...ypoints) - 4);
	num.setAttribute('class', 'num');

	const cap = document.createElementNS(ns, "text");
	cap.innerHTML = (office.people?.length || 0) + " / " + capacity;

	cap.setAttribute("x", ave(xpoints));
	cap.setAttribute("y", ave(ypoints) - 7);
	cap.setAttribute('class', 'cap');
	return [poly, num, cap];
}

async function setupMap(evans, officeNums) {

	const floors = floorDataToObject(evans.floors);

	evans.offices.forEach(office => {
		if (!officeNums.includes(office.number))
			return;

		const elements = createOfficeSvgElements(office);
		floors[office.floor].g.append(...elements);
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

	window.goToFloor = goToFloor;

	goToFloor(10);

	window.onresize = resize;
	imgmap.onload = resize;
	setTimeout(resize, 200);

	window.addEventListener("keydown", (evt) => "78901".includes(evt.key) && goToFloor(evt.key));
}

function setupDrawList(blocks) {
	blocks.forEach((block) => {
		const div = document.createElement("div");
		div.className = "block";
		block.done = block.people.every(x => x.office);
		div.dataset.done = block.done ? 1 : 0;
		div.dataset.searchable = block.people.map(person => person.name).join(", ").toLowerCase();
		const time = block.time == -1 ? "" : (block.time
			? new Date(block.time).toLocaleTimeString([], {
				hour: "numeric",
				minute: "numeric",
			})
			: "(squating)");
		const priority = block.people[0].priority;
		div.innerHTML = `
            <div>${time} (priority ${priority})</div>
            ${block.people.map((person) => `<div>${person.name}</div>`).join("")}
        `;
		drawOrder.lastElementChild.append(div);
		block.elmt = div;
	});

	setTimeout(
		() =>
			blocks.find((block) => !block.done)?.elmt.scrollIntoView({ behavior: "smooth" }),
		200
	);
}

function setupOfficeList(offices) {
	let highlightedOfficeNum = null;

	function highlightOfficeBox(number) {
		const old = document.querySelector(`polygon.office[data-number="${highlightedOfficeNum}"]`);
		if (old) old.dataset.highlighted = 0;

		highlightedOfficeNum = number;
		goToFloor(number[0]);
		const el = document.querySelector(`polygon.office[data-number="${number}"]`);
		if (!el) return;
		el.dataset.highlighted = 1;
		setTimeout(() => (el.dataset.highlighted = 0), 2000);
	}

	offices.forEach(({ number, people, capacity }) => {
		const div = document.createElement("div");
		div.className = "block";
		div.dataset.searchable = people?.join(", ").toLowerCase() || "";
		div.dataset.number = number;
		div.onclick = highlightOfficeBox.bind(null, div.dataset.number);

		div.innerHTML = `
        <div>Office ${number} (${people?.length || 0} / ${capacity})</div>
        ${people?.map((person) => `<div>${person}</div>`).join("") || ""}
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

function highlightOfficeInList(number) {
	scrollToOffice(number);
	setTimeout(scrollOffOffice.bind(null, number), 2000);
}

function searchForName(el, text) {
	text = text.toLowerCase();
	Array.from(el.children).forEach((child) => {
		if (child.dataset.searchable.includes(text))
			child.classList.remove('hidden')
		else
			child.classList.add('hidden')
	});
}

function processPeople(people, blocks, offices) {
	people.forEach(person => {
		const office = offices.find(office => office.number == person.office) || {};
		if (!office.people) office.people = [];
		office.people.push(person.name);

		if (!(person.index in blocks))
			blocks[person.index] = { people: [], time: parseInt(person.time) };
		blocks[person.index].people.push(person);

	});
}

async function fetchData(year) {
	const data = await fetch('data/data.json').then(res => res.json()).then(x => x.data.find(y => y.year == year) || x.data[x.data.length - 1]);
	const evans = await fetch('data/' + data.floorplan).then(x => x.json());
	const officeNums = await fetch('data/' + data.activeoffices).then(res => res.json()).then(x => x.offices);
	const people = await fetch('data/' + data.people).then(res => res.json()).then(x => x.people);
	return { evans, officeNums, people };
}

async function go() {
	const urlParams = new URLSearchParams(window.location.search);
	const msIn6Mo = 15768000000;
	const year = urlParams.get('year') || new Date(Date.now() - msIn6Mo).getFullYear();
	const { evans, officeNums, people } = await fetchData(year);

	const blocks = [];
	processPeople(people, blocks, evans.offices);

	setupMap(evans, officeNums);
	setupDrawList(blocks);
	setupOfficeList(evans.offices);

	window.searchForNameInDraw = searchForName.bind(null, drawOrder.lastElementChild);
	window.searchForNameInOffice = searchForName.bind(null, setOffices.lastElementChild);
	if (window.innerHeight > window.innerWidth)
		switchToMobileLayout();
}
