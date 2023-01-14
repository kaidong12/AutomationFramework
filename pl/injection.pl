#!/usr/bin/perl
#Author: Lance Yan
#Description: Run this script will insert a link in the Testng report, clicking this link will open our detailed log report.

#$outputdir = "C:\\Myfiles\\workspace\\docsite\\test-output";
#$logfile   = "../TestResults/DocSite_2013-05-07_11-40-07.html";
#&injection( $outputdir, $logfile );

use File::Copy;
&injection( $ARGV[0], $ARGV[1] );

sub injection {

	my ( $projectdir, $log ) = @_;
	my $outputdir = $projectdir . "\\test-output";
	my $jsdir     = $projectdir . "\\js";
	my $logfile   = "Logs/" . $log;
	$logfile      = "<a href=\"$logfile\">(show)</a>\n";
	$indexFile    = "$outputdir\\index.html";
	$newindexFile = "$outputdir\\$log";

	$emailableFile    = "$outputdir\\emailable-report.html";
	$newemailableFile = "$outputdir\\Emailable-Report-$log";

	#print "$indexFile";
	if ( -e $indexFile ) {
		unlink($indexFile);
	}
	select( undef, undef, undef, 3 );
	my $counter = 1;
	until ( -e $indexFile or $counter > 30 ) {
		select( undef, undef, undef, 1 );
		$counter++;
	}

	open( HTML,   "$indexFile" )     || die "Error opening Script.$!";
	open( HTMLTo, ">$newindexFile" ) || die "Error opening Script.$!";
	@myhtml = <HTML>;
	foreach $line (@myhtml) {
		if ( $line =~ /<span class="method-stats">/i ) {
			$line = $line . "$logfile\n";
		}
		if ( $line =~ /https:\/\/www.google.com\/jsapi/i ) {
			$line = "<script type=\"text/javascript\" src=\"jsapi.js\"></script>\n";
		}
		print HTMLTo "$line";
	}
	close HTMLTo;
	close HTML;
	$|++;
	$| = 1;

	copy( $jsdir . "\\jsapi.js", $outputdir . "\\jsapi.js" )
	  || die "Failed copy jsapi file! $!";
	copy( $emailableFile, $newemailableFile )
	  || die "Failed copy emailable report! $!";
	system("cmd /c start $newindexFile");

}

#<>;
