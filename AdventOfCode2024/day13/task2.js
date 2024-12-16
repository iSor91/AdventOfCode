
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
    // machines[machine]['p'] = data[i].match(/\d+/g).map(n => +n);
    machines[machine]['p'] = data[i].match(/\d+/g).map(n => +n + 10000000000000);
  } else {
    machine++
    machines[machine] = {}
  }
}

console.log(machines)

const treshold = 0.001
// 89013607072065
// 89013607072065
const getPrize = (m) => {

  const change = m['a'][0] / m['a'][1]
  const bAdd = m['b'][1] * change
  const resAdd = m['p'][1] * change
  // console.log(change, bAdd, resAdd)

  const bRes = m['b'][0] - bAdd
  const pRes = m['p'][0] - resAdd


  const bPushes = pRes / bRes
  if (bPushes - Math.floor(bPushes) < treshold || Math.ceil(bPushes) - bPushes < treshold) {
    const aPushes = (m['p'][1] - bPushes * m['b'][1]) / m['a'][1]

    if (aPushes - Math.floor(aPushes) < treshold || Math.ceil(aPushes) - aPushes < treshold) {
      // console.log(Math.round(aPushes), Math.round(bPushes))
      return 3 * Math.round(aPushes) + Math.round(bPushes)
    }
    else {
      console.log(m, bPushes, aPushes)
    }
  }
  else {
    console.log(m, bPushes)
  }

}

let tokens = 0

for (m in machines) {
  if (machines[m]['a']) {
    const minToNormalPrize = getPrize(machines[m])
    if (minToNormalPrize !== undefined)
      tokens += minToNormalPrize

  }
}

console.log(tokens)
