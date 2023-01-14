#!/usr/bin/perl
#Author: Lance Yan
#Description: paste all the sql string used by a specific test case to table sql files


print  "Please input the test case ID which you want to paste.\n";
$caseid = <stdin>;
chomp ($caseid);

if($caseid eq ""){
    exit;
}

$casename = "QAtestCMS_$caseid";

#$sqldir = "C:\\sql";
$sqldir = "C:\\Myfiles\\workspace\\Automotive\\sql";
$bytabledir = "$sqldir\\bytable";
$casesqlfile = "$sqldir\\other\\$casename.txt";

print  "Are you sure you want to paste all the sql used by $casename to table file?\nEnter for yes, press any key for No\n";
$selected = <stdin>;
chomp ($selected);
if($selected ne ""){
    exit;
}

open (CASESQL,"$casesqlfile") || die "Error opening Script.$!";
@mysql = <CASESQL>;
@tablefiles = ();
foreach $line(@mysql){
          if ($line =~ /^INSERT/){
                    @strarray = split /\"/, $line;
                    $tablename =  $strarray[1];
                    print "$tablename\n";
                    $tablefile = "$bytabledir\\$tablename.txt";
                    if(!($tablefile =~ @tablefiles)){
                         push(@tablefiles,$tablefile);
                    }
                    open (SQLTo,">>$tablefile") || die "Error opening Script.$!";
                    print SQLTo "$line" ;
                    close SQLTo;
          }
}
close CASESQL;

foreach $tablefile(@tablefiles){
         open (SQLTo,">>$tablefile") || die "Error opening Script.$!";
         print SQLTo "\n\n" ;
         close SQLTo;
}

$|++;
$|=1;
#<>;