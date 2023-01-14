#!/usr/bin/perl
#Author: Lance Yan
#Description: transfer sql string from bytable to bycase

$sqldir     = "C:\\Myfiles\\workspace\\Automotive\\sql";
$bytabledir = "$sqldir\\bytable";
$bycasedir  = "$sqldir\\bycase";

print
"Are you sure you want to transfer sql string from bytable to bycase?\nY for yes, press any key for No\n";
$selected = <stdin>;
chomp($selected);
if ( $selected ne "Y" ) {
	exit;
}

opendir( $DIRHANDLE, "$bytabledir" ) || die "Error opening :$!";
@tablelist = readdir($DIRHANDLE);
foreach $file (@tablelist) {
	if ( $file =~ /txt$/ ) {
		open( SQLFILE, "$bytabledir\\$file" ) || die "Error opening Script.$!";
		@sqlstrings = <SQLFILE>;
		foreach $mysql (@sqlstrings) {
			if ( $mysql =~ /^INSERT/ and $mysql =~ /QAtest/ ) {
				@strarray      = split /\"/, $mysql;
				$casename      = $strarray[3];
				@casenamearray = split /_/, $casename;
				$casefilename  = "$casenamearray[0]-$casenamearray[1].txt";

				open( CASEFILE, ">>$bycasedir\\$casefilename" )
				  || die "Error opening Script.$!";
				print CASEFILE "$mysql";
				close CASEFILE;
			}
			elsif ( $mysql =~ /^CREATE/ and $mysql =~ /TestCaseName/ ) {
				open( CASEFILE, ">>$bycasedir\\create_tables.txt" )
				  || die "Error opening Script.$!";
				print CASEFILE "$mysql";
				close CASEFILE;
			}
		}
		close SQLFILE;
	}
}
close DIRHANDLE;
$| = 1;

opendir( $DIRHANDLE, "$bycasedir" ) || die "Error opening :$!";
@tablefilelist = readdir($DIRHANDLE);
foreach $file (@tablefilelist) {
	if ( $file =~ /txt$/ ) {
		print "$file\n";
		open( SQLFILE, ">>$bycasedir\\$file" ) || die "Error opening Script.$!";
		print SQLFILE "\n\n";
		close SQLFILE;
	}
}
close DIRHANDLE;

$| = 1;
$|++;

#<>;
