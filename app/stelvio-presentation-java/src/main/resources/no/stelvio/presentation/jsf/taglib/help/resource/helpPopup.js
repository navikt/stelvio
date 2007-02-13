function openWindow(url, name, width, height) {
	// window decoration space
	width += 32;
	height += 96;
	var win = window.open(url, name, 'width=' + width + ', height=' + height +
				'location=no, menubar=no, status=no, toolbar=no, scrollbars=no, resizable=yes');
	win.resizeTo(width, height);
	win.focus();
};