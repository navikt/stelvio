function addLoadEvent(func) {
	var oldonload = window.onload;
	if (typeof window.onload != 'function') {
		window.onload = func;
	} else {
		window.onload = function() {
			if (oldonload) {
				oldonload();
			}
			func();
		}
	}
}
function setFocusOnLoad(formName, elementName) {
  addLoadEvent(function() {
	var f = document.forms[formName];
	if (f) {
		var elem = f.elements[elementName];
		if (elem) elem.focus();
	}});
}

function setFocusOnLoadById(elementId) {
  addLoadEvent(function() {
	var f = document.getElementById(elementId);
	if (f) {
		f.focus();
	}});
}

function setEditable(id, mode)
{
	if( document.getElementById )
	{
		var editable = mode;
		if ( editable == true )	document.getElementById(id).disabled = false; 
		else document.getElementById(id).disabled = true; 
	}
}


function getVisibility(id)
{
	if( document.getElementById )
	{
		var style = document.getElementById(id).style;
        return style;
	}
}
function setVisibility(id, visibility)
{
	if( document.getElementById )
	{
		document.getElementById(id).style.display = visibility;
	}
}
function enableChangePersonopplysninger()
{
	setVisibility('PPKnappEndrePersonopplysninger1', "inline");
	setVisibility('PPKnappEndrePersonopplysninger2', "inline");
	setVisibility('PPKnappLagreAvbrytPersonopplysninger1', "none");
	setVisibility('PPKnappLagreAvbrytPersonopplysninger2', "none");
}

function disableChangePersonopplysninger()
{
	setVisibility('PPKnappEndrePersonopplysninger1', "none");
	setVisibility('PPKnappEndrePersonopplysninger2', "none");
	setVisibility('PPKnappLagreAvbrytPersonopplysninger1', "block");
	setVisibility('PPKnappLagreAvbrytPersonopplysninger2', "block");
}

function setEditPersonopplysninger( mode )
{
	var visibility = 'inline';
	var inverse_visibility = 'none';

	if (mode == false)
	{
		visibility = 'none';
		inverse_visibility = 'inline';
	}


	setVisibility('meld_navneendring_lenke', visibility);
	setVisibility('meld_sivilstandsendring_lenke', visibility);
	setVisibility('meld_flytting_lenke', visibility);

	setEditable('adr_utland_gate_id', mode);
	setEditable('adr_utland_postnr_id', mode);
	setEditable('adr_utland_poststed_id', mode);
	setEditable('adr_utland_land_id', mode);
	setEditable('adr_utland_fra_dato_id', mode);
	setVisibility('adr_utland_kalender_enabled_id', visibility);
	setVisibility('adr_utland_kalender_disabled_id', inverse_visibility);

	setEditable('tilleggsadr_norge_gate_id', mode);
	setEditable('tilleggsadr_norge_postnr_id', mode);
	setEditable('tilleggsadr_norge_poststed_id', mode);
	setEditable('tilleggsadr_norge_fra_dato_id', mode);
	setEditable('tilleggsadr_norge_til_dato_id', mode);
	setVisibility('tilleggsadr_norge_kalender_enabled_id', visibility);
	setVisibility('tilleggsadr_norge_kalender_disabled_id', inverse_visibility);
	setVisibility('tilleggsadr_norge_kalender_enabled_id2', visibility);
	setVisibility('tilleggsadr_norge_kalender_disabled_id2', inverse_visibility);

	setEditable('tilleggsadr_utland_gate_id', mode);
	setEditable('tilleggsadr_utland_postnr_id', mode);
	setEditable('tilleggsadr_utland_poststed_id', mode);
	setEditable('tilleggsadr_utland_land_id', mode);
	setEditable('tilleggsadr_utland_fra_dato_id', mode);
	setEditable('tilleggsadr_utland_til_dato_id', mode);
	setVisibility('tilleggsadr_utland_kalender_enabled_id', visibility);
	setVisibility('tilleggsadr_utland_kalender_disabled_id', inverse_visibility);
	setVisibility('tilleggsadr_utland_kalender_enabled_id2', visibility);
	setVisibility('tilleggsadr_utland_kalender_disabled_id2', inverse_visibility);

	setEditable('folkeregistrert_adresse_norge_id', mode);
	setEditable('bostedsadresse_utland_id', mode);
	setEditable('tilleggsadresse_norge_id', mode);
	setEditable('tilleggsadresse_utland_id', mode);

	setEditable('tlfnr_privat_land_id', false);
	setEditable('tlfnr_privat_id', mode);
	setEditable('tlfnr_arbeid_land_id', mode);
	setEditable('tlfnr_arbeid_id', mode);
	setEditable('mobnr_land_id', mode);
	setEditable('mobnr_id', mode);
	setEditable('epost_id', mode);

	setEditable('malform_id1', mode);
	setEditable('malform_id2', mode);
	setEditable('distribusjonskanal_id1', mode);
	setEditable('distribusjonskanal_id2', mode);

	setVarsling(getCheckedValue(distribusjonskanal));
}

function changePersonopplysninger()
{
	setEditPersonopplysninger( true );
	disableChangePersonopplysninger();
}

function cancelChangePersonopplysninger()
{
	setEditPersonopplysninger( false );
	enableChangePersonopplysninger();
}

function savePersonopplysninger()
{
	cancelChangePersonopplysninger();
}

function setVarsling(mode)
{
	var alertEditable = false;

	if (mode == 'elektronisk') alertEditable = true;

	setEditable('varslingskanal_id1', alertEditable);
	setEditable('varslingskanal_id2', alertEditable);
	setEditable('varslingskanal_id3', alertEditable);
}

function setFormDisabled()
{
	setEditPersonopplysninger( false );
}

function setUtvandret(mode)
{
	var visibility_norsk = 'block';
	var visibility_utenlandsk = 'none';
	var inline_visibility_utenlandsk = 'none';

	if ( mode == false ) 
	{
		visibility_norsk = 'block';
		visibility_utenlandsk = 'none';
	}
	else
	{
		visibility_norsk = 'none';
		visibility_utenlandsk = 'block';
		inline_visibility_utenlandsk = 'inline';
	}

	setVisibility('folkeregistrert_adresse_norge', visibility_norsk);
	setVisibility('bostedsadresse_utland', visibility_utenlandsk);
	setVisibility('info_postadresse_norsk', visibility_norsk);
	setVisibility('info_postadresse_utenlandsk', visibility_utenlandsk);
	setVisibility('tlfnr_privat_land_id', inline_visibility_utenlandsk);
	setVisibility('tlfnr_arbeid_land_id', inline_visibility_utenlandsk);
}


function pageLoad()
{
	setFormDisabled();
	setUtvandret(false);
}


function changeVisibility(id)
{
	if( document.getElementById )
	{
		var style = document.getElementById(id).style;
		if( style.display == "none" ) { style.display = "block"; }
		else if( style.display == "block" ) { style.display = "none"; }
	}
}

function disableCheckbox(checkboxId)
{
	if( document.getElementById )
	{
		if( document.getElementById(checkboxId).disabled ) 
		{ 
			document.getElementById(checkboxId).disabled = false; 
		}
		else
		{
			document.getElementById(checkboxId).disabled = true; 
			document.getElementById(checkboxId).checked = false; 
		}
	}
}
function getCheckedValue(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked)
			return radioObj.value;
		else
			return "";
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}

function validateFnr(fieldId, errorMsgId)
{
	if( document.getElementById )
	{
		var value = document.getElementById(fieldId).value;
		if( value.length == 0 || value.length == 11 )
		{  
			document.getElementById(errorMsgId).style.display = "none";
		}
		else
		{
			document.getElementById(errorMsgId).style.display = "block";
			document.getElementById(fieldId).focus();
		}
	}
}
function showWhatIf(dropdownId, max)
{
	if( document.getElementById )
	{
		var dropdown = document.getElementById(dropdownId);
		var idx = dropdown.selectedIndex;
		for (i = 0; i <= max; i++)
		{
			if( i == dropdown.options[idx].value ){ document.getElementById(dropdownId +'_' + i).style.display = "block"; }
			else { document.getElementById(dropdownId +'_' + i).style.display = "none"; }
		}
	}
}
function setVisibility(id, visibility)
{
	if( document.getElementById )
	{
		document.getElementById(id).style.display = visibility;
	}
}

function changeScreenSize(w,h)
{
	window.resizeTo(w,h)
}

function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}

function MM_openBrWindow(theURL,winName,features) { //v2.0
  win=window.open(theURL,winName,features);
  win.moveTo(300,300);
}

function sendForm( button ) {
  document.getElementById( button ).click();
}
