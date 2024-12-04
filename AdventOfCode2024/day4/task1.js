
const fs = require('fs')

const lines = fs.readFileSync('input', 'utf-8').split('\n').filter(l => l.length !== 0).map((l) =>
  l.split('')
)

const charMap = {
  'X': [],
  'M': [],
  'A': [],
  'S': []
}

lines.forEach((line, i) => {
  line.forEach((char, j) => {
    console.log(char, i, j)
    charMap[char].push([i, j])
  })
});

console.log(charMap)

const directions = {
  left: [],
  right: [],
  up: [],
  down: [],
  tlbr: [],
  trbl: [],
  brtl: [],
  bltr: []
}

const findSuitable = (charNum, next) => {
  let prev = directions['left'][charNum - 1]
  if (next[0] === prev?.[0] && prev?.[1] - 1 === next[1]) {
    directions['left'].push(next)
  }
  prev = directions['right'][charNum - 1]
  if (next[0] === prev?.[0] && prev?.[1] + 1 === next[1]) {
    directions['right'].push(next)
  }
  prev = directions['up'][charNum - 1]
  if (next[0] === prev?.[0] - 1 && prev?.[1] === next[1]) {
    directions['up'].push(next)
  }
  prev = directions['down'][charNum - 1]
  if (next[0] === prev?.[0] + 1 && prev?.[1] === next[1]) {
    directions['down'].push(next)
  }
  prev = directions['tlbr'][charNum - 1]
  if (next[0] === prev?.[0] + 1 && prev?.[1] + 1 === next[1]) {
    directions['tlbr'].push(next)
  }
  prev = directions['trbl'][charNum - 1]
  if (next[0] === prev?.[0] + 1 && prev?.[1] - 1 === next[1]) {
    directions['trbl'].push(next)
  }
  prev = directions['brtl'][charNum - 1]
  if (next[0] === prev?.[0] - 1 && prev?.[1] - 1 === next[1]) {
    directions['brtl'].push(next)
  }
  prev = directions['bltr'][charNum - 1]
  if (next[0] === prev?.[0] - 1 && prev?.[1] + 1 === next[1]) {
    directions['bltr'].push(next)
  }
}

const foundWords = []

charMap['X'].forEach(first => {
  console.log(directions)
  Object.keys(directions).forEach(x => {
    if (directions[x].length === 4) {
      foundWords.push(directions[x])
    }
    directions[x] = []
  })
  Object.keys(directions).forEach(f => directions[f].push(first))
  charMap['M'].forEach(second => {
    findSuitable(1, second)
  })
  charMap['A'].forEach(third => {
    findSuitable(2, third)
  })
  charMap['S'].forEach(forth => {
    findSuitable(3, forth)
  })
})

Object.keys(directions).forEach(x => {
  if (directions[x].length === 4) {
    foundWords.push(directions[x])
  }
  directions[x] = []
})

const flattened = foundWords.flatMap(f => f)
console.log(flattened)

lines.forEach((l, i) => {
  console.log()
  l.forEach((c, j) => {
    if (flattened.filter(x => x[0] === i && x[1] === j).length !== 0) {
      process.stdout.write(c)
    } else {
      process.stdout.write('.')
    }
  })
})

console.log()
console.log(foundWords.length)
