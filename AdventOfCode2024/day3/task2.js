
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split("\n").filter(e => e.length !== 0)

const regex = new RegExp("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don\\'t\\(\\)", 'g')

const matches = data.flatMap(l => {
  return [...l.matchAll(regex)];
})

console.log(matches)
// .map(l => l.map(r => [r[1], r[2]]))
//   .map(l => l.map(r => +r[0] * +r[1]))
//   .map(l => l.reduce((previous, current) => {
//     return current + previous
//   }, 0)).reduce((p, c) => p + c, 0)
//

let enabled = true
let res = 0
for (f of matches) {
  if (f[0] === 'do()') {
    enabled = true
    console.log(enabled)
  }
  else if (f[0] === "don't()") {
    enabled = false
    console.log(enabled)
  }
  else {
    if (enabled) {
      res += +f[1] * +f[2]
    }
  }
}

console.log(res)

