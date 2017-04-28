$(document).ready(function()
{
  $(document).on('click','a',handleAnchor);
});

var handleAnchor = function(event)
{
  event.preventDefault();
  var target = $(event.target);
  var page = target.attr('href');
  var type = target.attr('data-type');
  if(page != "#" && !type)
  {
    $.get(page,function(data,status,xhr)
    {
      $('#content').html(data);
      initParts();
    });
  }
  else if(type == 'external')
  {
    window.location = page;
  }
}

var initParts = function()
{
  $('div[data-load]').each(function()
  {
     var load = $(this).attr('data-load');
     var target = $(this);
     $.get(load,function(data,status,xhr)
     {
       $(target).replaceWith(data);
     });
  });
}
