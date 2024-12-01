
const fs = require('node:fs')

const data = fs.readFileSync('input', 'utf-8')

const lines = data.split("\n").filter(l => l.length !== 0);

const value = lines.map(l => {
  return [...l.matchAll("\\d")]
}).map(element => {
  return `${element[0][0]}${element[element.length - 1][0]}`
})
  .reduce((i, c) => {
    return +i + +c
  }, 0)

console.log(value)
