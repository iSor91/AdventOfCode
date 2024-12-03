
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split("\n").filter(e => e.length !== 0)

const regexStr = 'mul\((\\d+),(\\d+)\)'
const regex = new RegExp('mul\\((\\d+),(\\d+)\\)', 'g')

const matches = data.map(l => {
  return [...l.matchAll(regex)];
}).map(l => l.map(r => [r[1], r[2]]))
  .map(l => l.map(r => +r[0] * +r[1]))
  .map(l => l.reduce((previous, current) => {
    return current + previous
  }, 0)).reduce((p, c) => p + c, 0)

console.log(matches)

