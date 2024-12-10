
const fs = require('fs')

const starts = []

const data = fs.readFileSync('input', 'utf-8')
  .split('\n')
  .filter(l => l.length !== 0)
  .map((l, i) =>
    l.split('')
      .map((c, j) => {
        if (+c === 0) {
          starts.push([i, j])
        }
        return +c
      }))

console.log(starts)

const height = (curr) => {
  return data[curr[0]][curr[1]]
}

const nextSteps = (curr) => {
  const up = [curr[0] - 1, curr[1]]
  const down = [curr[0] + 1, curr[1]]
  const left = [curr[0], curr[1] - 1]
  const right = [curr[0], curr[1] + 1]

  return [up, down, left, right]
    .filter(p => p[0] >= 0 && p[1] >= 0 && p[0] < data.length && p[1] <= data[0].length)
    .filter(p => height(p) === height(curr) + 1)
}

let trails = starts.map(t => [t])
console.log(trails)
let finishedTrails = []

while (trails.length !== 0) {
  const newTrails = []
  for (let trail of trails) {
    for (let next of nextSteps(trail[trail.length - 1])) {
      if (height(next) === 9) {
        finishedTrails.push([...trail, next])
      } else {
        newTrails.push([...trail, next])
      }
    }
  }
  trails = newTrails
}

const startScores = {}
finishedTrails.forEach(trail => {
  let finishes = startScores[trail[0]]
  if (!finishes) {
    finishes = []
  }
  if (!finishes.filter(f => f[0] === trail[9][0] && f[1] === trail[9][1])[0]) {
    finishes.push(trail[9])
  }
  startScores[trail[0]] = finishes
})

// console.log(finishedTrails)

let sum = 0
console.log(startScores)
for (s in startScores) {
  sum += startScores[s].length
}

console.log(sum)
