var lp = '<h3> Rank30_List of V(i,j) <\/h3> <table cellpadding="10">';
lp += '<tr> <td> <table border="1">';
lp += '<tr> <td>RANK<\/td> <td>Pos<\/td> <td>V(i,j)<\/td> <td>W(i,j)<\/td> <td>P(i,j)<\/td> <td>Lift(i,j)<\/td> <\/tr>';
for(var i=0;i<bag.length/3;i++)
{
	var rank = i+1;
	var posx = parseInt(bag[i][1])+1;
	var posy = parseInt(bag[i][2])+1;
	lp += '<tr>';
	lp += '<td>' + rank + '<\/td>';
	lp += '<td>(' + posx + ',' + posy + ')<\/td>';
 	lp += '<td>' + bag[i][0] + '<\/td>';
	lp += '<td>' + bag[i][3] + '<\/td>';
	lp += '<td>' + bag[i][4] + '<\/td>';
	lp += '<td>' + bag[i][5] + '<\/td>';
	lp += '<\/tr>';
}
lp += '<\/table> <\/td>'

lp += '<td> <table border="1">';
lp += '<tr> <td>RANK<\/td> <td>Pos<\/td> <td>V(i,j)<\/td> <td>W(i,j)<\/td> <td>P(i,j)<\/td> <td>Lift(i,j)<\/td> <\/tr>';
for(var i=bag.length/3;i<bag.length*2/3;i++)
{
	var rank = i+1;
	var posx = parseInt(bag[i][1])+1;
	var posy = parseInt(bag[i][2])+1;
	lp += '<tr>';
	lp += '<td>' + rank + '<\/td>';
	lp += '<td>(' + posx + ',' + posy + ')<\/td>';
 	lp += '<td>' + bag[i][0] + '<\/td>';
	lp += '<td>' + bag[i][3] + '<\/td>';
	lp += '<td>' + bag[i][4] + '<\/td>';
	lp += '<td>' + bag[i][5] + '<\/td>';
	lp += '<\/tr>';
}
lp += '<\/table> <\/td>'

lp += '<td> <table border="1">';
lp += '<tr> <td>RANK<\/td> <td>Pos<\/td> <td>V(i,j)<\/td> <td>W(i,j)<\/td> <td>P(i,j)<\/td> <td>Lift(i,j)<\/td> <\/tr>';
for(var i=bag.length*2/3;i<bag.length;i++)
{
	var rank = i+1;
	var posx = parseInt(bag[i][1])+1;
	var posy = parseInt(bag[i][2])+1;
	lp += '<tr>';
	lp += '<td>' + rank + '<\/td>';
	lp += '<td>(' + posx + ',' + posy + ')<\/td>';
 	lp += '<td>' + bag[i][0] + '<\/td>';
	lp += '<td>' + bag[i][3] + '<\/td>';
	lp += '<td>' + bag[i][4] + '<\/td>';
	lp += '<td>' + bag[i][5] + '<\/td>';
	lp += '<\/tr>';
}
lp += '<\/td> <\/tr> <\/table>'
document.getElementById('bag').innerHTML = lp;