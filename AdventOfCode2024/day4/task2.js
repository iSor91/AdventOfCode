
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
    charMap[char].push([i, j])
  })
});

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

const findSuitable = (char, next) => {
  let prev = directions['left'][0]
  if (
    char === 'M' && ((next[0] === prev?.[0] - 1 && prev?.[1] - 1 === next[1])
      || (next[0] === prev?.[0] + 1 && prev?.[1] - 1 === next[1]))
    ||
    char === 'S' && ((next[0] === prev?.[0] - 1 && prev?.[1] + 1 === next[1])
      || (next[0] === prev?.[0] + 1 && prev?.[1] + 1 === next[1]))
  ) {
    directions['left'].push(next)
  }
  prev = directions['right'][0]
  if (
    char === 'M' && ((next[0] === prev?.[0] - 1 && prev?.[1] + 1 === next[1])
      || (next[0] === prev?.[0] + 1 && prev?.[1] + 1 === next[1]))
    ||
    char === 'S' && ((next[0] === prev?.[0] - 1 && prev?.[1] - 1 === next[1])
      || (next[0] === prev?.[0] + 1 && prev?.[1] - 1 === next[1]))
  ) {
    directions['right'].push(next)
  }
  prev = directions['up'][0]
  if (
    char === 'M' && ((next[0] === prev?.[0] + 1 && prev?.[1] - 1 === next[1])
      || (next[0] === prev?.[0] + 1 && prev?.[1] + 1 === next[1]))
    ||
    char === 'S' && ((next[0] === prev?.[0] - 1 && prev?.[1] - 1 === next[1])
      || (next[0] === prev?.[0] - 1 && prev?.[1] + 1 === next[1]))
  ) {
    console.log(char, prev, next)
    directions['up'].push(next)
  }
  prev = directions['down'][0]
  if (
    char === 'M' && ((next[0] === prev?.[0] - 1 && prev?.[1] - 1 === next[1])
      || (next[0] === prev?.[0] - 1 && prev?.[1] + 1 === next[1]))
    ||
    char === 'S' && ((next[0] === prev?.[0] + 1 && prev?.[1] - 1 === next[1])
      || (next[0] === prev?.[0] + 1 && prev?.[1] + 1 === next[1]))
  ) {
    directions['down'].push(next)
  }
  // prev = directions['tlbr'][0]
  // if (
  //   ((next[0] === prev?.[0] - 1 && prev?.[1] === next[1])
  //     || (next[0] === prev?.[0] && prev?.[1] - 1 === next[1])
  //     && char === 'M')
  //   ||
  //   ((next[0] === prev?.[0] + 1 && prev?.[1] === next[1])
  //     || (next[0] === prev?.[0] && prev?.[1] + 1 === next[1])
  //     && char === 'S')
  // ) {
  //   directions['tlbr'].push(next)
  // }
  // prev = directions['trbl'][0]
  // if (
  //   ((next[0] === prev?.[0] - 1 && prev?.[1] === next[1])
  //     || (next[0] === prev?.[0] && prev?.[1] + 1 === next[1])
  //     && char === 'M')
  //   ||
  //   ((next[0] === prev?.[0] + 1 && prev?.[1] === next[1])
  //     || (next[0] === prev?.[0] && prev?.[1] - 1 === next[1])
  //     && char === 'S')
  // ) {
  //   directions['trbl'].push(next)
  // }
  // prev = directions['brtl'][0]
  // if (
  //   ((next[0] === prev?.[0] + 1 && prev?.[1] === next[1])
  //     || (next[0] === prev?.[0] && prev?.[1] + 1 === next[1])
  //     && char === 'M')
  //   ||
  //   ((next[0] === prev?.[0] - 1 && prev?.[1] === next[1])
  //     || (next[0] === prev?.[0] && prev?.[1] - 1 === next[1])
  //     && char === 'S')
  // ) {
  //   directions['brtl'].push(next)
  // }
  // prev = directions['bltr'][0]
  // if (
  //   ((next[0] === prev?.[0] + 1 && prev?.[1] === next[1])
  //     || (next[0] === prev?.[0] && prev?.[1] - 1 === next[1])
  //     && char === 'M')
  //   ||
  //   ((next[0] === prev?.[0] - 1 && prev?.[1] === next[1])
  //     || (next[0] === prev?.[0] && prev?.[1] + 1 === next[1])
  //     && char === 'S')
  // ) {
  //   directions['bltr'].push(next)
  // }
}

const foundWords = []

charMap['A'].forEach(first => {
  console.log(directions)
  Object.keys(directions).forEach(a => {
    if (directions[a].length === 5) {
      foundWords.push(directions[a])
    }
    directions[a] = []
  })
  Object.keys(directions)
    .forEach(a => directions[a].push(first))
  charMap['M'].forEach(second => {
    findSuitable('M', second)
  })
  charMap['S'].forEach(third => {
    findSuitable('S', third)
  })
})

Object.keys(directions).forEach(x => {
  console.log(x, directions[x])
  if (directions[x].length === 5) {
    foundWords.push(directions[x])
  }
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
