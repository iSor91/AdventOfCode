
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split("\n");

const pageOrderInput = []
const pagesToProduceInput = []

let dividerHappened = false
data.forEach(element => {
  if (dividerHappened) {
    pagesToProduceInput.push(element)
  }
  else if (element.length === 0) {
    dividerHappened = true
  }
  else {
    pageOrderInput.push(element)
  }
});

const pagesToProduce = pagesToProduceInput.filter(l => l.length !== 0).map(l => l.split(',').map(p => +p))
const pageOrder = pageOrderInput.map(e => e.split('|')).map(e => [+e[0], +e[1]])

console.log(pageOrder)
console.log(pagesToProduce)

const validUpdates = pagesToProduce.filter(
  update => {
    let updateValid = true
    for (page of update) {
      const pagesAfterThis = pageOrder.filter(o => o[0] === page).map(o => o[1]).map(p => update.indexOf(p)).filter(i => i !== -1)
      const pagesBeforeThis = pageOrder.filter(o => o[1] === page).map(o => o[0]).map(p => update.indexOf(p)).filter(i => i !== -1)

      const pageIndex = update.indexOf(page)

      console.log(pageIndex, pagesAfterThis, pagesBeforeThis)

      if (pagesAfterThis.some(p => p < pageIndex) || pagesBeforeThis.some(p => p > pageIndex))
        updateValid = false
    }
    return updateValid
  }
)

console.log(validUpdates)
console.log(validUpdates.map(update => update[Math.floor(update.length / 2)]).reduce((p, c) => p + c, 0))
