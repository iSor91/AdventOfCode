
const fs = require('node:fs')

const data = fs.readFileSync('input', 'utf-8')

const lines = data.split("\n").filter(l => l.length !== 0);

const numbers = ['zero', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine']
const regex = `\\d|${numbers.join("|")}`

const value = lines.map(l => {
  const res = []
  var m
  while (m = regex.exec(l)) {
    res.push(m[0])
    regex.lastIndex = m.index + 1
  }
  return [...l.matchAll(regex)]
})
  .map(element => {
    return element.map(e => {
      // if (e[0].match('\\d') === null) {
      //   return numbers.indexOf(e[0])
      // }
      return e[0]
    })
  })
  .map(current => `${current[0]}${current[current.length - 1]}`)
//   .reduce((previous, current) => {
//     return previous + +`${current[0]}${current[current.length - 1]}`
//   }, 0)
// console.log(value)

const content = []
for (let i = 0; i < lines.length; i++) {
  content.push(`${lines[i]}, ${value[i]}`)
}

fs.writeFile('output', content.join('\n'), (err) => console.log(err))

