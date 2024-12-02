

const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split("\n").filter(e => e.length !== 0).map((report) => {
  return report.split(' ').map(i => +i)
});

console.log(data)

const safeAnalysis = data.map(report => {
  const tmp = report.map((e, i) => {
    if (i === report.length)
      return [e, e]
    else
      return [e, report[i + 1]]
  }).map(e => e[0] - e[1])
  return tmp.slice(0, tmp.length - 1);
}).filter(report => report.every(e => e > 0) || report.every(e => e < 0))
  .map(report => {
    console.log(report)
    return !report.some(e => Math.abs(e) > 3)
  }).filter(e => e)


console.log(safeAnalysis.length)
