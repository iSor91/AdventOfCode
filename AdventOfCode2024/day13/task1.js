
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split('\n');

const costs = { 'a': 3, 'b': 1 }
const machines = { 0: {} }

let machine = 0

for (let i = 0; i < data.length; i++) {
  if (i % 4 === 0) {
    machines[machine]['a'] = data[i].match(/\d+/g).map(n => +n);
  }
  else if (i % 4 === 1) {
    machines[machine]['b'] = data[i].match(/\d+/g).map(n => +n);
  }
  else if (i % 4 === 2) {
    machines[machine]['p'] = data[i].match(/\d+/g).map(n => +n);
  } else {
    machine++
    machines[machine] = {}
  }
}

console.log(machines)

const add = (pos, step, cost) => {
  return [pos[0] + step[0], pos[1] + step[1], pos[2] + cost]
}

const nextPos = (pos, move, m, toVisit) => {
  // console.log(pos, m[move], m['p'], toVisit)
  const nextA = add(pos, m[move], costs[move])
  if (nextA[0] <= m['p'][0]
    && nextA[1] <= m['p'][1]
    && !toVisit.filter(n => n[0] === nextA[0]
      && n[1] === nextA[1]
      && n[2] <= nextA[2])[0]
  )
    return [nextA]
  return []
}

const getPrize = (m) => {
  let toVisit = [[0, 0, 0]]
  let minToPrize = -1
  while (toVisit.length > 0) {
    const current = toVisit[0]
    toVisit.push(...nextPos(current, 'a', m, toVisit))
    toVisit.push(...nextPos(current, 'b', m, toVisit))
    toVisit = toVisit.slice(1)
    // console.log(toVisit)
    if (current[0] === m['p'][0] && current[1] === m['p'][1] && (current[2] < minToPrize || minToPrize === -1)) {
      minToPrize = current[2]
    }
  }
  return minToPrize
}

let tokens = 0

for (m in machines) {
  if (machines[m]['a']) {
    console.log(machines[m])
    const minToGetPrize = getPrize(machines[m])
    if (minToGetPrize !== -1) {
      tokens += minToGetPrize
    }
  }
}

console.log(tokens)
