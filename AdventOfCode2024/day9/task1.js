
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8')

const files = []
const freeSpaces = []
data.split('').map((c, i) => {
  if (i % 2 === 0) {
    files.push(+c)
  }
  else {
    freeSpaces.push(+c)
  }
})

console.log(files, freeSpaces)

const representation = []
for (let i = 0; i < files.length; i++) {
  for (let j = 0; j < files[i]; j++) {
    representation.push(i)
  } for (let j = 0; j < freeSpaces[i] ?? 0; j++) {
    representation.push('.')
  }
}

// console.log(representation.join(''))
console.log(representation.length)

let i = representation.length - 1
console.log(i)
for (; i > 0; i--) {
  const toMove = representation[i]
  if (toMove !== '.') {
    const moveTo = representation.indexOf('.')
    console.log(toMove, moveTo)
    if (moveTo >= i) {
      break
    }
    representation[moveTo] = representation[i]
    representation[i] = '.'
    // console.log(representation.join(''))
  }
}

// console.log(representation.join(''))

let sum = 0
representation.forEach((c, i) => {
  if (c !== '.') {
    sum += c * i
  }
})
console.log(sum)

