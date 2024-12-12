

const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8')
  .split('\n').filter(l => l.length !== 0)
  .map(l => l.split(''))

console.log(data)

const visited = []
const plots = {}

const contains = (arr, pos) => {
  return arr.filter(v => v[0] === pos[0] && v[1] === pos[1])[0]
}

const getAllNeighbours = (pos) => {
  const up = [pos[0] - 1, pos[1]]
  const down = [pos[0] + 1, pos[1]]
  const left = [pos[0], pos[1] - 1]
  const right = [pos[0], pos[1] + 1]
  return [up, down, left, right]
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
    // console.log(notVisited)
    toVisit.push(...notVisited)
    plot.push(current)
    visited.push(current)
    toVisit = toVisit.slice(1)
    // console.log('tovisit', toVisit, visited)
  }
  return plot
}

const mapToPerimeter = (arr) => {
  return arr.map(p => {
    return getAllNeighbours(p).filter(n => !contains(arr, n)).length
  });

}

for (let i = 0; i < data.length; i++) {
  for (let j = 0; j < data[i].length; j++) {
    if (!contains(visited, [i, j])) {
      const plot = mapPlot(data[i][j], [i, j])
      const typeIndex = Object.keys(plots).filter(p => p.startsWith(data[i][j])).length
      plots[`${data[i][j]}${typeIndex}`] = plot
    }
  }
}

// console.log(plots)

const fencePrice = Object.keys(plots).map(p => {
  return plots[p].length * mapToPerimeter(plots[p]).reduce((prev, current) => {
    return prev + current
  }, 0)
}).reduce((p, c) => p + c, 0)

console.log(fencePrice)
