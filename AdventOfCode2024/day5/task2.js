
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

const invalidUpdates = pagesToProduce.filter(
  update => {
    let updateValid = true
    for (page of update) {
      const pagesAfterThis = pageOrder.filter(o => o[0] === page).map(o => o[1]).map(p => update.indexOf(p)).filter(i => i !== -1)
      const pagesBeforeThis = pageOrder.filter(o => o[1] === page).map(o => o[0]).map(p => update.indexOf(p)).filter(i => i !== -1)

      const pageIndex = update.indexOf(page)

      if (pagesAfterThis.some(p => p < pageIndex) || pagesBeforeThis.some(p => p > pageIndex))
        updateValid = false
    }
    return !updateValid
  }
)

const fixedordering = invalidUpdates.map(update => {
  let updatedOrdering = update
  for (page of update) {
    console.log(updatedOrdering.join(','))
    const pagesAfterThis = pageOrder.filter(o => o[0] === page).map(o => o[1]).map(p => updatedOrdering.indexOf(p)).filter(i => i !== -1)
    pagesAfterThis.sort((a, b) => a - b)
    const pagesBeforeThis = pageOrder.filter(o => o[1] === page).map(o => o[0]).map(p => updatedOrdering.indexOf(p)).filter(i => i !== -1)
    pagesBeforeThis.sort((a, b) => a - b)

    const pageIndex = updatedOrdering.indexOf(page)

    const tmpOrdering = []

    if (pagesAfterThis.some(p => p < pageIndex) || pagesBeforeThis.some(p => p > pageIndex)) {

      console.log(pagesBeforeThis.join(','), pageIndex, pagesAfterThis.join(','))
      pagesBeforeThis.forEach(p => tmpOrdering.push(updatedOrdering[p]))
      tmpOrdering.push(page)
      pagesAfterThis.forEach(p => tmpOrdering.push(updatedOrdering[p]))

      updatedOrdering = tmpOrdering
    }
  }
  console.log(updatedOrdering.join(','))
  console.log()
  return updatedOrdering
})


// console.log(fixedordering)
console.log(fixedordering.map(update => update[Math.floor(update.length / 2)]).reduce((p, c) => p + c, 0))
