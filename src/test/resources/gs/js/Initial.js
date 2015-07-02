function Initial() {
  var gSobject = gs.inherit(gs.baseClass,'Initial');
  gSobject.clazz = { name: 'gs.src.main.groovy.gs.Initial', simpleName: 'Initial'};
  gSobject.clazz.superclass = { name: 'java.lang.Object', simpleName: 'Object'};
  gSobject.prop = null;
  if (arguments.length == 1) {gs.passMapToObject(arguments[0],gSobject);};
  
  return gSobject;
};
