
const fs = require('fs')
let data = fs.readFileSync('input', 'utf-8').split('\n').map(l => l.split(''))

const obstacles = []
let startingPos
let startingDir = [0, 0]

const add = (t1, t2) => {
  return [t1[0] + t2[0], t1[1] + t2[1]]
}

const turn = (dir) => {
  return [dir[1], -dir[0]]
}

const step = () => {
  const nextPos = add(startingPos, startingDir)
  if (includes(obstacles, nextPos)) {
    startingDir = turn(startingDir)
  } else {
    startingPos = nextPos
  }
}

const includes = (list, tuple) => {
  return list.filter(
    o => o[0] === tuple[0]
      && o[1] === tuple[1]
  )[0]
}

const getDir = (dir) => {
  if (dir[0] == 0 && dir[1] === -1)
    return '<'
  if (dir[0] == 0 && dir[1] === 1)
    return '>'
  if (dir[0] == 1 && dir[1] === 0)
    return 'v'
  if (dir[0] == -1 && dir[1] === 0)
    return '^'
  return 'X'
}

data = data.map((l, row) => {
  return l.map((c, col) => {
    if (c == '#') {
      obstacles.push([row, col])
    }
    else if (c === '.') {
    } else {
      startingPos = [row, col]
      if (c === '>') {
        startingDir = [0, 1]
      }
      if (c === '<') {
        startingDir = [0, -1]
      }
      if (c === '^') {
        startingDir = [-1, 0]
      }
      if (c === 'v') {
        startingDir = [1, 0]
      }
      return '.'
    }
    return c
  })
})

const positions = []

while (startingPos[0] >= 0
  && startingPos[0] < data.length - 1
  && startingPos[1] >= 0
  && startingPos[1] < data[0].length - 1) {
  if (!includes(positions, startingPos)) {
    positions.push([...startingPos, startingDir])
  }
  step()
}

data.forEach((e, i) => {
  console.log(e.map((c, j) => {
    if (includes(obstacles, [i, j])) {
      return '#'
    } else if (includes(positions, [i, j])) {
      const dir = getDir(includes(positions, [i, j])[2])
      return dir
    } else return c
  }).join(''))
});

// const posStrings = positions.map(p => `${getDir(p[2])} - ${p[0]},${p[1]}`)
// posStrings.forEach(s => console.log(s))
// console.log([...new Set(posStrings)].length)
// console.log(startingPos)
console.log(positions.length)
