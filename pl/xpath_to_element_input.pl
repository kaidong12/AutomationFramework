#!/usr/bin/perl
#Author: Lance Yan
#Date: 2013-04-14
#Description: Convert xpath string in the file with the name which you input in <pages> folder to WebElement object.
#######################################################
######Please put this script in your xpath folder######
#######################################################

use File::Copy;
use Cwd;
my $currentdir = getcwd;
@path = split( "/", $currentdir );
$xpathdir = join( "\\", @path );
pop(@path);
$pagesdir = join( "\\", @path );

&xpath2element( $xpathdir, $pagesdir );

sub xpath2element {
	my ($xpathdir, $pagesdir) = @_;
	
	if ( !-e $xpathdir ) {
		print "Folder:\t$xpathdir \ndoes not exist!";
		exit;
	}
	if ( !-e $pagesdir ) {
		print "Folder:\t$pagesdir \ndoes not exist!";
		exit;
	}

	print "Please input the xpath file(without extentions) which you want to convert.\n";
	$pageName = <stdin>;
	chomp($pageName);
	if ( $pageName eq "" ) {
		exit;
	}

	#my $pageName     = "HomePage";
	my $pageFileName      = "$pageName.java";
	my $bakFileName       = "$pageName.java.bak";
	my $tempFileName      = "$pageName.java.tmp";
	my $fullJavaFileName  = "$pagesdir\\$pageFileName";
	my $fullXpathFileName = "$xpathdir\\I$pageFileName";
	my $fullbakFileName   = "$pagesdir\\$bakFileName";
	my $fulltmpFileName   = "$pagesdir\\$tempFileName";

	print "Are you sure you want to convert xpath in interface file: \n"
	  . "$fullXpathFileName \nto class file: \n$fullJavaFileName \n? \n"
	  . "Enter for yes, press any key for No\n ";
	my $YesorNo = <stdin>;
	chomp($YesorNo);
	if ( $YesorNo ne "" ) {
		exit;
	}

	if ( !-e $fullXpathFileName ) {
		print "File:\t$fullXpathFileName \ndoes not exist!";
		exit;
	}

	open( XPATH, "$fullXpathFileName" ) || die "Error opening xpath file.$!";
	open( CLASS, "$fullJavaFileName" )  || die "Error opening class file.$!";
	open( TEMP,  ">$fulltmpFileName" )  || die "Error opening class file.$!";
	my @xpaths  = <XPATH>;
	my @classes = <CLASS>;

	foreach (@classes) {
		print TEMP $_;
		print TEMP "\n" if ( $_ =~ /}$/ );
		last if ( $_ =~ /}$/ );
	}

	my @constructor = (
		"indent", "public", "container",    "interface",
		"{\n",    "indent", "\treturn new", "Welcome();\n",
		"indent", "}\n\n"
	);
	my @container = (
		"indent",    "public class", "container", "implements",
		"interface", "{\n"
	);
	my @element = (
		"indent",
		"public XWebElement",
		"string",
		"{\n",
		"indent", "\t_xelement.set_elements(driver.findElements(By.xpath(xpath)));\n",
		"indent", "\t_xelement.setName(\"name\");\n",
		"indent", "\t_xelement.setLocator(string);\n",
		"indent", "\treturn", "_xelement;\n",
		"indent", "}\n\n"
	);

	#my $depth  = 1;
	my @parent = ($pageName);
	my @indent = ("\t");

	foreach $line (@xpaths) {
		if ( $line =~ /\tinterface / ) {
			my $indent1 = join( "", @indent );
			$container[0]   = $indent1;
			$constructor[0] = $indent1;
			$constructor[5] = $indent1;
			$constructor[8] = $indent1;
			push( @indent, "\t" );

			local @name = split( " ", $line );
			$container[2]   = $name[1];
			$constructor[2] = $name[1];
			$constructor[3] = join( ".", "panel\$$name[1]()" );

			push( @parent, $name[1] );
			$container[4] = "I" . join( ".", @parent );

			$constructor[7] = "$name[1]();\n";

			print TEMP join( " ", @constructor );
			print TEMP join( " ", @container );

			#$line = print "$line";
			#$depth += 1;
			#print "$depth" . "\n";

		}

		if ( $line =~ /\tString/ ) {
			my $indent2 = join( "", @indent );
			$element[0]  = $indent2;
			$element[4]  = $indent2;
			$element[6]  = $indent2;
			$element[8]  = $indent2;
			$element[10] = $indent2;
			$element[13] = $indent2;

			local @name = split( " ", $line );

			unless( $name[0] =~ /^\/\//){
				if ( $name[3] =~ /^\"\/\// ) {
					if ( $line =~ /%TEXT%/ ) {
						my $total = @{[$line =~ m/%TEXT%/g]};
						if($total>=2){
							$element[2] = "$name[1](String... text)";
						}else{
							$element[2] = "$name[1](String text)";
						}
						$element[5] = "\t_xelement.set_elements(driver.findElements(By.xpath(getReplacedString($name[1], text))));\n";
						$element[9] = "\t_xelement.setLocator(getReplacedString($name[1], text));\n";
					} else {
						$element[2] = "$name[1]()";
						$element[5] = "\t_xelement.set_elements(driver.findElements(By.xpath($name[1])));\n";
						$element[9] = "\t_xelement.setLocator($name[1]);\n";
					}
				} else {
					if( $line =~ /:contains\(/ or $line =~ /:first-child/ or $line =~ /:last-child/ or $line =~ /:nth-child\(/ or $line =~ /:first/ or $line =~ /:last/ ){
						if ( $line =~ /%TEXT%/ ) {
							my $total = @{[$line =~ m/%TEXT%/g]};
							if($total>=2){
								$element[2] = "$name[1](String... text)";
							}else{
								$element[2] = "$name[1](String text)";
							}
							$element[5] = "\t_xelement.set_elements(driver.findElements(XBy.cssSelector(getReplacedString($name[1], text))));\n";
							$element[9] = "\t_xelement.setLocator(getReplacedString($name[1], text));\n";
						} else {
							$element[2] = "$name[1]()";
							$element[5] = "\t_xelement.set_elements(driver.findElements(XBy.cssSelector($name[1])));\n";
							$element[9] = "\t_xelement.setLocator($name[1]);\n";
						}
					}else{
						if ( $line =~ /%TEXT%/ ) {
							my $total = @{[$line =~ m/%TEXT%/g]};
							if($total>=2){
								$element[2] = "$name[1](String... text)";
							}else{
								$element[2] = "$name[1](String text)";
							}
							$element[5] = "\t_xelement.set_elements(driver.findElements(By.cssSelector(getReplacedString($name[1], text))));\n";
							$element[9] = "\t_xelement.setLocator(getReplacedString($name[1], text));\n";
						} else {
							$element[2] = "$name[1]()";
							$element[5] = "\t_xelement.set_elements(driver.findElements(By.cssSelector($name[1])));\n";
							$element[9] = "\t_xelement.setLocator($name[1]);\n";
						}
					}
				}
			}
			$location = join( "->", @parent );
			$element[7] = "\t_xelement.setName(\"$location->$name[1]\");\n";
			$element[9] = "\t_xelement.setLocator($name[1]);\n";
			print TEMP join( " ", @element );

			#print "$depth" . "\n";

		}

		if ( $line =~ /}$/ ) {
			pop(@parent);
			pop(@indent);
			print TEMP "$line\n\n";

			#$depth -= 1;
			#print "$depth" . "\n";

		}

	}

	$|++;
	$| = 1;

	close(XPATH) || die "Failed close file! $!";
	close(CLASS) || die "Failed close file! $!";
	close(TEMP)  || die "Failed close file! $!";

	copy( $fullJavaFileName, $fullbakFileName ) || die "Failed copy file! $!";
	move( $fulltmpFileName, $fullJavaFileName ) || die "Failed copy file! $!";

	#<>;

}
