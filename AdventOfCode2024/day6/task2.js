
const fs = require('fs')
let data = fs.readFileSync('input', 'utf-8').split('\n').map(l => l.split(''))

//setup
const obstacles = []
let startingPos
let startingDir = [0, 0]

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



//base functions
const add = (t1, t2) => {
  return [t1[0] + t2[0], t1[1] + t2[1]]
}

const turn = (dir) => {
  return [dir[1], -dir[0]]
}

const step = (obs, currentPos, currentDir) => {
  const nextPos = add(currentPos, currentDir)
  if (includes(obs, nextPos)) {
    return [currentPos, turn(currentDir)]
  }
  return [nextPos, currentDir]
}

const includes = (list, tuple, dir) => {
  return list.filter(
    o => o[0] === tuple[0]
      && o[1] === tuple[1]
      && (!dir || o[2][0] === dir[0]
        && o[2][1] === dir[1])
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

const navigate = (obs, sp, sd, positionsUntilNow) => {
  let currentPos = [sp[0], sp[1]]
  let currentDir = sd
  let currentPositions = [...positionsUntilNow]

  while (currentPos[0] >= 0
    && currentPos[0] < data.length - 1
    && currentPos[1] >= 0
    && currentPos[1] < data[0].length
    && !includes(currentPositions, currentPos, currentDir)) {
    currentPositions.push([...currentPos, currentDir])
    const ret = step(obs, currentPos, currentDir)
    currentPos = ret[0]
    currentDir = ret[1]
  }
  return [currentPos, currentPositions, includes(currentPositions, currentPos, currentDir)]
}


let possibilities = 0
for (let i = 0; i < data.length - 1; i++) {
  for (let j = 0; j < data[0].length; j++) {
    console.log([i, j], possibilities)
    const updatedObstacles = [...obstacles, [i, j]]
    const [end, _, cycle] = navigate(updatedObstacles, startingPos, startingDir, [])
    if (cycle) {
      possibilities++
    }
  }
}

console.log(possibilities)

// const [gettingOut, positions] = navigate(obstacles, startingPos, startingDir, [])
// const newObstaclePositions = []
// let possibilities = 0
// positions.forEach((p, i) => {
//   const obstaclePlace = add([p[0], p[1]], p[2])
//   const nextIsObstacle = includes(obstacles, obstaclePlace)
//   if (nextIsObstacle ||
//     obstaclePlace[0] < 0 || obstaclePlace[0] >= data.length - 1
//     || obstaclePlace[1] < 0 || obstaclePlace[1] >= data[0].length - 1
//     || obstaclePlace[0] === startingPos[0] && obstaclePlace[1] === startingPos[1]) {
//     return
//   }
//
//   const nextDir = turn(p[2])
//   const [newEnd, newPositions, circle] = navigate([...obstacles, obstaclePlace], p, nextDir, positions.slice(0, i))
//   if (circle) {
//     console.log(newEnd, i)
//     if (!newObstaclePositions.filter(o => o[0] === obstaclePlace[0] && o[1] === obstaclePlace[1])[0])
//       newObstaclePositions.push(obstaclePlace)
//     possibilities++
//   }
// })

// console.log(possibilities)
// console.log(newObstaclePositions.length)
