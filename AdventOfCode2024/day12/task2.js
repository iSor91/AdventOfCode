

const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8')
  .split('\n').filter(l => l.length !== 0)
  .map(l => l.split(''))

const visited = []
const plots = {}

const contains = (arr, pos) => {
  return arr.filter(v => v[0] === pos[0] && v[1] === pos[1])[0]
}

const getVerticalNeighbours = (pos) => {
  const up = [pos[0] - 1, pos[1]]
  const down = [pos[0] + 1, pos[1]]
  return [up, down]
}

const getHorizontalNeighbours = (pos) => {
  const left = [pos[0], pos[1] - 1]
  const right = [pos[0], pos[1] + 1]
  return [left, right]
}

const getAllNeighbours = (pos) => {
  return [...getVerticalNeighbours(pos), ...getHorizontalNeighbours(pos)]
}

const getNeighbours = (type, pos) => {
  return getAllNeighbours(pos).filter(p => {
    return p[0] >= 0 && p[0] < data.length && p[1] >= 0 && p[1] < data[0].length && data[p[0]][p[1]] === type
  })
}

const mapPlot = (type, [i, j]) => {
  let toVisit = [[i, j]]
  const plot = []
  while (toVisit.length > 0) {
    const current = toVisit[0]
    const neighBours = getNeighbours(type, toVisit[0])
    const notVisited = neighBours.filter(n => !contains(toVisit, n) && !contains(visited, n))
    toVisit.push(...notVisited)
    plot.push(current)
    visited.push(current)
    toVisit = toVisit.slice(1)
  }
  return plot
}

const mapToSides = (arr) => {
  const horizontalSidesUp = {}
  const horizontalSidesDown = {}
  const verticalSidesLeft = {}
  const verticalSidesRight = {}

  arr.forEach(pos => {
    const up = [pos[0] - 1, pos[1]]
    const down = [pos[0] + 1, pos[1]]
    const left = [pos[0], pos[1] - 1]
    const right = [pos[0], pos[1] + 1]

    if (!contains(arr, up)) {
      if (!horizontalSidesUp[pos[0]]) {
        horizontalSidesUp[pos[0]] = []
      }
      horizontalSidesUp[pos[0]].push(pos)
    }
    if (!contains(arr, down)) {
      if (!horizontalSidesDown[pos[0]]) {
        horizontalSidesDown[pos[0]] = []
      }
      horizontalSidesDown[pos[0]].push(pos)
    }
    if (!contains(arr, left)) {
      if (!verticalSidesLeft[pos[1]]) {
        verticalSidesLeft[pos[1]] = []
      }
      verticalSidesLeft[pos[1]].push(pos)
    }
    if (!contains(arr, right)) {
      if (!verticalSidesRight[pos[1]]) {
        verticalSidesRight[pos[1]] = []
      }
      verticalSidesRight[pos[1]].push(pos)
    }
  });

  console.log('up', horizontalSidesUp)
  console.log('down', horizontalSidesDown)
  console.log('left', verticalSidesLeft)
  console.log('right', verticalSidesRight)

  let sides = 0
  Object.keys(horizontalSidesUp).map(row => {
    const currentRow = horizontalSidesUp[row].sort((a, b) => a[1] - b[1]);
    console.log(currentRow)
    for (let c = 0; c < currentRow.length; c++) {
      const current = currentRow[c]
      if (!contains(currentRow, [current[0], current[1] + 1])) {
        sides++
      }
    }
  })
  Object.keys(horizontalSidesDown).map(row => {
    const currentRow = horizontalSidesDown[row].sort((a, b) => a[1] - b[1]);
    console.log(currentRow)
    for (let c = 0; c < currentRow.length; c++) {
      const current = currentRow[c]
      if (!contains(currentRow, [current[0], current[1] + 1])) {
        sides++
      }
    }
  })
  Object.keys(verticalSidesLeft).map(col => {
    const currentCol = verticalSidesLeft[col].sort((a, b) => a[0] - b[0]);
    for (let c = 0; c < currentCol.length; c++) {
      const current = currentCol[c]
      if (!contains(currentCol, [current[0] + 1, current[1]])) {
        sides++
      }
    }
  })
  Object.keys(verticalSidesRight).map(col => {
    const currentCol = verticalSidesRight[col].sort((a, b) => a[0] - b[0]);
    for (let c = 0; c < currentCol.length; c++) {
      const current = currentCol[c]
      if (!contains(currentCol, [current[0] + 1, current[1]])) {
        sides++
      }
    }
  })

  return sides
}

const plotSides = {}
for (let i = 0; i < data.length; i++) {
  for (let j = 0; j < data[i].length; j++) {
    if (!contains(visited, [i, j])) {
      const plot = mapPlot(data[i][j], [i, j])
      const typeIndex = Object.keys(plots).filter(p => p.startsWith(data[i][j])).length
      const id = `${data[i][j]}${typeIndex}`
      console.log(id)
      plots[id] = plot
      plotSides[id] = mapToSides(plot)
    }
  }
}

let result = 0

Object.keys(plots).forEach(p => {
  result += plots[p].length * plotSides[p]
})

console.log(plotSides)
console.log(result)
