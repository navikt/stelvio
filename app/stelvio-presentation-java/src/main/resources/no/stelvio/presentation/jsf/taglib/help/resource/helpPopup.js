var win = '';
function openWindow(url, name, width, height) {
	if(!win.closed && win.location) {
		win.location.href = url;
	} else {
		win = window.open(url, name, 'width=' + width + ', height=' + height);
		if(!win.opener) win.opener = self;
	}
	if(win.focus) {
		win.focus();
	}
	return false;
}
//
//	
