

const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split("\n").filter(e => e.length !== 0).map((report) => {
  return report.split(' ').map(i => +i)
});

const analyse = (toAnalyse) => {
  const tmp = toAnalyse.map((e, i) => {
    return [e, toAnalyse[i + 1], e - toAnalyse[i + 1]]
  })
  return tmp.slice(0, tmp.length - 1);
}

const analyse2 = (toAnalyse) => {
  const t = toAnalyse.flatMap(report => report[2])
  return [t.filter(e => e < 0 && e >= -3).length,
  t.filter(e => e > 0 && e <= 3).length]
}

const isSafe = (analysed, i) => {
  return analysed.some(e => e === i)
}

const safeAnalysis = data.map(report => analyse(report))


let safes = []
for (let i = 0; i < data.length; i++) {
  const r = safeAnalysis[i]
  const analysed = analyse2(r)
  if (isSafe(analysed, data[i].length - 1)) {
    safes.push(data[i])
  } else {
    for (let j = 0; j < data[i].length; j++) {
      const reconstructed = [
        ...data[i].slice(0, j),
        ...data[i].slice(j + 1, data[i].length)
      ]
      const r2 = analyse(reconstructed)
      const analysed2 = analyse2(r2)
      if (isSafe(analysed2, reconstructed.length - 1)) {
        safes.push([data[i], reconstructed, analysed2])
        break
      }
    }
  }
}


console.log([...new Set(safes)].length)

// const tryRemoveSafe = (toRemove, i) => {
//   const reconstructed = [
//     ...data[i].slice(0, toRemove),
//     ...data[i].slice(toRemove + 1, data[i].length)
//   ]
//   const safeAnalysis2 = analyse(reconstructed)
//   const analysed2 = analyse2(safeAnalysis2)
//   if (analysed2.some(e => e === reconstructed.length - 1)) {
//     console.log(data[i], safeAnalysis2, analysed2, 'safe')
//     return true
//   }
//   return false
// }
//
//
//
// console.log(`analysed`, analysed)
// let problems = safeAnalysis[i]
//   .filter(e =>
//     e[2] > 3 ||
//     e[2] < -3 ||
//     e[2] === 0 ||
//     analysed[0] === 1 && e[2] < 0 ||
//     analysed[1] === 1 && e[2] > 0)
//
// let safe = false
// for (let j = 0; j < problems.length && !safe; j++) {
//   const toRemove = safeAnalysis[i].indexOf(problems[j])
//   if (tryRemoveSafe(toRemove, j)) {
//     safe = true
//   }
// }
// if (safe) {
//   console.log(data[i], 'safe')
//   safes += 1
// } else {
//   console.log(data[i], 'unsafe')
// }
