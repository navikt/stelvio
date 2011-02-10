use strict;
use CQPerlExt;


my $headline = @ARGV[0];
my $description = @ARGV[1];

my $cquserdb = "NAV";
my $cqlogin = "admin";
my $cqpasswd = "Solo1234";
my $cqschema = "7.0.0";

my $CQSession = CQSession::Build() or die "Error: $!";

$CQSession->UserLogon($cqlogin, $cqpasswd, $cquserdb, $cqschema);
my $newTask = $CQSession->BuildEntity("task");
$newTask->SetFieldValue("headline", $headline);
$newTask->SetFieldValue("description", $description);
my $id = $newTask->GetFieldValue("id")->GetValue();
my $status = $newTask->Validate();
if ($status){
	$newTask->Revert();
	die "Error validating: $status \n";
} else { $newTask->Commit(); }

print "[INFO] Created new task with ID: " . $id;
CQSession::Unbuild($CQSession);

open( OUTFILE, ">cleartool_output.txt");
print OUTFILE $id;
close(OUTFILE);