document.addEventListener('DOMContentLoaded', function () {

	var tripDateSelector = document.getElementsByClassName('tripDateSelector')[0];
	
	if (tripDateSelector != null) {
		tripDateSelector.addEventListener('input', function() {
			    if (this.value) {
			        const date = new Date(this.value);
			        if (!isNaN(date.getTime())) {
			            this.classList.remove('border-danger', 'text-danger');
			            this.classList.add('border-secondary', 'text-secondary');
			        }
			    }
			});
	}
	
});
	
function showAlert(message) {
	alert(message);
}

