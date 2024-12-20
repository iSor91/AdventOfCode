
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8')
  .split("\n")
  .filter(l => l.length !== 0);

const contains = (arr, pos) => {
  return arr.filter(a => a[0] === pos[0] && a[1] === pos[1])[0]
}

const frequency = (arr) => {
  const set = [...new Set(arr)]
  return set.map(s => [s, arr.filter(a => a === s).length])
}

const parts = data
  .flatMap((l, y) =>
    l.split('').map((c, x) => [y, x, c]))
  .filter(p => p[2] !== '#')

console.log(parts)

const start = parts.filter(p => p[2] === 'S')[0]
let current = start

const path = []
while (current[2] !== 'E') {
  // for (let i = 0; i < 10; i++) {
  path.push(current)

  current = parts.filter(p =>
    ((p[0] === current[0] && (p[1] === current[1] + 1 || p[1] === current[1] - 1))
      ||
      (p[1] === current[1] && (p[0] === current[0] + 1 || p[0] === current[0] - 1)))
    && !contains(path, p)
  )[0]
}
path.push(current)

const baseLenght = path.length

const cheats = []

for (let i = 0; i < path.length; i++) {
  current = path[i]

  const cheat = parts.filter(p =>
  ((p[0] === current[0]
    && (p[1] === current[1] + 2 || p[1] === current[1] - 2))
    ||
    (p[1] === current[1] && (p[0] === current[0] + 2 || p[0] === current[0] - 2)))
  ).map(p => path.indexOf(p))
    .filter(ci => ci > i + 2)

  if (cheat.length > 0) {
    console.log(i, current, cheat, cheat.map(c => c - i - 2))
    cheats.push([current, cheat.map(c => c - i - 2)])
  }
}

const saves = cheats.flatMap(c => c[1])
// console.log(saves)
// .filter(p => p > 100)
const frequencies = frequency(saves)

console.log(saves.filter(s => s >= 100).length)
