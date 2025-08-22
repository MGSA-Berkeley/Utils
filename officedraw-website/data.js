function setupDrawList(people) {
	people.forEach((person) => {
		const row = document.createElement("tr");
		const name = person.name;
		const office = person.office;
		row.innerHTML = `
			<td>${name}</td><td>${office}</td>
		`;
		draw.append(row);
	})
}

async function fetchData(year) {
	const data = await fetch('data/data.json?cachebust=' + Date.now()).then(res => res.json()).then(x => x.find(y => y.year == year) || x.data[x.data.length - 1]);
	const people = await fetch('data/' + data.people + '?cachebust=' + Date.now()).then(res => res.json());
	return people;
}

async function go() {
	const urlParams = new URLSearchParams(window.location.search);
	const msIn7Mo = 18396000000;
	const year = urlParams.get('year') || new Date(Date.now() - msIn7Mo).getFullYear();
	const people = await fetchData(year);
	setupDrawList(people);
}
