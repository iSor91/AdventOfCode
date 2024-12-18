
const fs = require('fs')

const target = [70, 70]
const corruptionNumber = 1024
const file = 'input'
//test
// const target = [6, 6]
// const corruptionNumber = 12
// const file = 'test0'

const data = fs.readFileSync(file, 'utf-8')
  .split('\n').filter(l => l.length !== 0)
  //input has data as x,y i'll use it as y,x
  .map(l => l.split(',').map(n => +n).reverse());

console.log(data)

const corrupted = data.slice(0, corruptionNumber);
console.log(corrupted)

const containsPos = (arr, pos) => {
  return arr.filter(a => pos[0] === a[0] && pos[1] === a[1])[0]
}

const add = (a, b) => {
  return [a[0] + b[0], a[1] + b[1]]
}

const dirs = [[0, 1], [1, 0], [0, -1], [-1, 0]]

const start = [0, 0]

let toVisit = [[...start, 0]]
let visited = []

while (toVisit.length > 0) {
  const current = toVisit[0]
  // console.log('current', current)
  if (current[0] === target[0] && current[1] === target[1]) {
    break
  }

  const nextSteps = dirs.map(dir => add(current, dir)).filter(
    //filter for inside the grid steps
    step => step[0] <= target[0] && step[1] <= target[1] && step[0] >= 0 && step[1] >= 0
  )
    .filter(step => !containsPos(corrupted, step))
    .filter(step => !containsPos(visited, step))
    .filter(step => !containsPos(toVisit, step))
    .map(step => [...step, current[2] + 1, current])

  const betterPaths = nextSteps.map(step => [step, containsPos(toVisit, step)])
    .filter(step => step[1] !== undefined)
    .filter(step => step[0][2] < step[1][2])

  console.log('updatePath', betterPaths)

  toVisit = toVisit.slice(1)
  visited.push(current)
  toVisit.push(...nextSteps)
  // console.log('tovisit', toVisit)
  toVisit = toVisit.sort((a, b) => a[2] - b[2])

}

console.log(toVisit[0])