
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split('\n').filter(l => l.length !== 0);

const antennas = data.flatMap((l, i) => l.split('').map((c, j) => {
  if (c !== '.') {
    return [c, i, j]
  }
  return null
})).filter(a => a !== null)

const antennaMap = Object.groupBy(antennas, i => i[0])
console.log(antennaMap)

const antinodes = Object.keys(antennaMap).flatMap(a => {
  const frequencyAntennas = antennaMap[a]
  const tmpAntinodes = []
  for (let i = 0; i < frequencyAntennas.length; i++) {
    const currentAntenna = frequencyAntennas[i]
    const otherAntennas = frequencyAntennas.filter(a => a !== currentAntenna)
    tmpAntinodes.push(...otherAntennas.flatMap(o => {
      const tmp = []
      for (s = 0; s < data.length; s++) {
        tmp.push([currentAntenna[1] - s * (o[1] - currentAntenna[1]), currentAntenna[2] - s * (o[2] - currentAntenna[2])])
      }
      console.log(tmp)
      return tmp
    }))

  }
  return tmpAntinodes
})


console.log(antinodes)
const distinctAntinodes = []

for (let i = 0; i < data.length; i++) {
  console.log(data[i].split('').map((c, j) => {
    const current = antinodes.filter(a => a[0] === i && a[1] === j)[0]
    if (current) {
      distinctAntinodes.push(`${current[0]},${current[1]}`)
      return '#'
    }
    return c
  }).join(''))
}

// console.log(Object.groupBy(antinodes, i => `${i[0]},${i[1]}`))

console.log(distinctAntinodes)
console.log(distinctAntinodes.length)
console.log([...new Set(distinctAntinodes)].length)
