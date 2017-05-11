$(document).ready(function()
{
  console.log("Load template");
  var templateObj = $("template");
  var root = templateObj.attr("data-root");
  var templateFile = root+"templates/"+templateObj.attr("data-template")+".html";
  var content = templateObj.html();
  $.get(templateFile,function(data,status,xhr)
  {
    console.log("template retrieved");
    document.write(data);
    var body = $(document).find("template");
    body.replaceWith(content);
  });
});
