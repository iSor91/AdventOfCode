
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

// console.log(files)

const representation = []
for (let i = 0; i < files.length; i++) {
  for (let j = 0; j < files[i]; j++) {
    representation.push(i)
  } for (let j = 0; j < freeSpaces[i] ?? 0; j++) {
    representation.push('.')
  }
}

// console.log(representation.join(''))

let i = files.length - 1
for (; i > 0; i--) {
  const toMove = files[i]
  const searchFor = Array(toMove).fill('\.').join('')
  const original = Array(toMove).fill(`${i}`).join('')
  const startIndex = representation.indexOf(i)
  const moveTo = representation.map(c => c === '.' ? '.' : '1').join('').indexOf(searchFor)
  console.log(i, searchFor, original, startIndex, moveTo)
  if (moveTo >= startIndex || moveTo === -1) {
    continue
  }
  for (let k = 0; k < searchFor.length; k++) {
    representation[moveTo + k] = `${i}`
  }
  for (let k = 0; k < searchFor.length; k++) {
    representation[startIndex + k] = '.'
  }

  // console.log(representation.join(''))
}

// console.log(representation.join(''))
// console.log(representation.join(''))

let sum = 0
representation.forEach((c, i) => {
  if (c !== '.') {
    sum += +c * i
  }
})
console.log(sum)

