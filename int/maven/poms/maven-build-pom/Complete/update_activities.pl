#use strict;
use CQPerlExt;

my $cquserdb = "NAV";
my $cqlogin = "admin";
my $cqpasswd = "Solo1234";
my $cqschema = "7.0.0";


sub isType{
	$cqrectyp = $_[0];
	$cqfield = "id";
	$cqfindval = $_[1];
	$Session = $_[2];
	
	$queryDefObj = $Session->BuildQuery($cqrectyp);
	$queryDefObj->BuildField($cqfield);
	$operator = $queryDefObj->BuildFilterOperator($CQPerlExt::CQ_COMP_OP_EQ);
	$operator->BuildFilter($cqfield, $CQPerlExt::CQ_COMP_OP_EQ, [$cqfindval]);
	$resultSetObj = $Session->BuildResultSet($queryDefObj);
	
	$resultSetObj->Execute();
	
	$count =0;
	$fetchStatus = $resultSetObj->MoveNext();
	while($fetchStatus eq "1"){
		$count = $count+1;
		$fetchStatus = $resultSetObj->MoveNext();
	}
	
	$count;
}


my $ids = @ARGV[0]; # list of all activities that have been delivered
my $build_id = @ARGV[1]; # application name and stream, e.g. PEN_HR
my $version = @ARGV[2]; # maven build version

my $CQSession = CQSession::Build() or die "Error: $!";
$CQSession->UserLogon($cqlogin, $cqpasswd, $cquserdb, $cqschema);
my $entity;

my @activities = split(',',$ids);

foreach my $id (@activities) {
	if(isType("Task", $id, $CQSession) > 0) {
		$entity = $CQSession->GetEntity("Task", $id);
	} 
	elsif(isType("Implementation", $id, $CQSession) > 0){
		$entity = $CQSession->GetEntity("Implementation", $id);
	} else {
		next;
	}
	my $headline = $entity->GetFieldValue("Headline")->GetValue();
	my $state = $entity->GetFieldValue("state")->GetValue();
	if ((index(lc($headline), "edit poms") >= 0) && (lc($state) ne "task_complete")) {
		# it is our pom edit task
		$CQSession->EditEntity($entity,"close");
	} else {
		$CQSession->EditEntity($entity,"Modify");
	}
	$entity->SetFieldValue("Build_id", $build_id);
	$entity->SetFieldValue("MVN_Version", $version);
	my $status = $entity->Validate();
	if ($status){
		$entity->Revert();
		die "Error validating: $status \n";
	} else { 
		$entity->Commit(); 
		#$entity->Revert();
		print "Done setting $build_id to Build_id field for $id\n";
	}
}
CQSession::Unbuild($CQSession);