const findOptionById = (options, type, id) => (
  options.get(type).find(x => x.get('id') === id)
);

export {findOptionById};