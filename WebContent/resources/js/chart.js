$(document).ready(function(){
    chartJs();
})


function chartJs() {
    
    let ctx = document.getElementById("myChart1").getContext('2d');
    let ctx2 = document.getElementById("myChart2").getContext('2d');
    let ctx3 = document.getElementById("myChart3").getContext('2d');
    
    let options = {
        legend: {
            //display: false,
            fullSize: false,
            position: 'bottom',
            textAlign: 'end',
        },
        maintainAspectRatio: true, // default value. false일 경우 포함된 div의 크기에 맞춰서 그려짐.
        scales: {
            xAxes: [{
                gridLines: {
                    display:false
                },
                borderDash: false,
                ticks: {
                    fontSize: 14,
                },
                barPercentage: 0.5
            }],
            yAxes: [{
                gridLines: {
                    display:false
                },
                ticks: {
                    beginAtZero:true,
                }
            }]
        }
    }
    
    let myChart1 = new Chart(ctx, {
    type: 'line',//doughnut
    data: {
        labels: ["접수대기","검토중","처리중","종결"],
        datasets: [{
            label: '# of Votes',
            data: [102,50,20,40],
            borderWidth: 3,
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(0, 0, 0, 0.05)',
            ],
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(0, 0, 0, 0.3)',
            ],
            borderWidth: 1
        }]},
        options: options
        
    });
    
    let myChart2 = new Chart(ctx2, {
    type: 'bar',
    data: {
        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
        datasets: [{
            label: '# of Votes',
            data: [12, 19, 3, 5, 2, 3],
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1
        }]},
        options: options
    });
    
    let myChart3 = new Chart(ctx3, {
    type: 'line',
		data: {
			labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
			datasets: [{
				label: '# of Votes',
				data: [12, 19, 3, 5, 2, 3],
                fill: false,
				backgroundColor: [
					'rgba(255, 99, 132, 0.2)',
					'rgba(54, 162, 235, 0.2)',
					'rgba(255, 206, 86, 0.2)',
					'rgba(75, 192, 192, 0.2)',
					'rgba(153, 102, 255, 0.2)',
					'rgba(255, 159, 64, 0.2)'
				],
				borderColor: [
					'rgba(255, 99, 132, 1)',
					'rgba(54, 162, 235, 1)',
					'rgba(255, 206, 86, 1)',
					'rgba(75, 192, 192, 1)',
					'rgba(153, 102, 255, 1)',
					'rgba(255, 159, 64, 1)'
				],
				borderWidth: 2
			}]
		},
        options: options
    });
}