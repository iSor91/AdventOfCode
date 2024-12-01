const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8')

const lines = data.split('\n')
  .map(line => line.split(' ')
    .filter(c => c.length !== 0)
    .map(c => +c))
  .filter(l => l.length !== 0);

const first = []
const second = []

lines.forEach(element => {
  first.push(element[0])
  second.push(element[1])
});

first.sort()
second.sort()

const dist = first.map((e, i) => [e, second[i]])
  .map(e => Math.abs(e[0] - e[1]))
  .reduce((p, c) => p + c, 0)

console.log(dist)

