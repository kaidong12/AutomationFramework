#!/usr/bin/perl
#Author: Lance Yan
#Date: 2013-05-30
#Description: Use this script when you want to replace the test case ID in current project with the test case ID in a mapping table.
#######################################################
######Please put this script in your xpath folder######
#######################################################

use Cwd;

my $logfile = "Core_Portal_CMS_2013-05-31_16-34-59.txt";

my $currentdir = getcwd;
@path = split( "/", $currentdir );
pop(@path);
$projectdir = join( "\\", @path );
my $logdir = $projectdir . "\\test-output\\Logs\\";
my $mappingfile = $projectdir . "\\src\\main\\java\\com\\covisint\\cms\\caseidmapping.txt";
my $resultfile = $logdir . $logfile;
my $newresultfile = $logdir . "x" . $logfile;

open( MAP, "$mappingfile" ) || die "Error opening mapping file.$!";
my %mapping;
while (<MAP>) {
	local @pair = split( " = ", $_ );
	$mapping{ $pair[0] } = $pair[1];
}

#while (($key, $value) = each(%mapping)){
#     print $key.", ".$value;
#}

open( RESULT,    "$resultfile" )   || die "Error opening results file.$!";
open( NEWRESULT, ">$newresultfile" ) || die "Error opening xresults file.$!";

while (<RESULT>) {
	if ( $_ =~ /=/ ) {
		local @pair = split( "=", $_ );
		local $key = $mapping{ $pair[0] };
		if ( !defined($key) ) {
			$key = $pair[0];
		}
		else {
			chomp($key);
		}

		local $str = "$key" . "=" . "$pair[1]";
		print $str;
		print NEWRESULT $str;

	}

}

$|++;
$| = 1;

print "Done!";

