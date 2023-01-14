#!/usr/bin/perl
#Author: Lance Yan
#Description: copy all the sql string used by a specific test case from table sql files


print  "Please input the test case ID which you want to copy.\n";
$caseid = <stdin>;
chomp ($caseid);

if($caseid eq ""){
    exit;
}

$casename = "QAtestCMS_$caseid";

$sqldir = "C:\\Myfiles\\workspace\\Automotive\\sql";
$bytabledir = "$sqldir\\bytable";
$casesqlfile = "$sqldir\\other\\$casename-copy.txt";

print  "Are you sure you want to copy sql string of $casename?\nEnter for yes, press any key for No\n";
$selected = <stdin>;
chomp ($selected);
if($selected ne ""){
    exit;
}

opendir ($DIRHANDLE,"$bytabledir") || die "Error opening :$!";
@filelist = readdir ($DIRHANDLE);
open (SQLTo,">>$casesqlfile") || die "Error opening Script.$!";
foreach $file(@filelist){
         if ($file =~ /txt$/){
                  print  $file."\n";
                  open (MySQL,"$bytabledir\\$file") || die "Error opening Script.$!";
                  @mysql = <MySQL>;
                  foreach $line (@mysql){
                           if($line =~ /$casename/i){
                               #print "$line";
                               #$line =~ s/c_name/e_name/g;
                               print SQLTo "$line" ;
                           }
                  }
                  close MySQL;
         }
}
print SQLTo "\n\n" ;
close SQLTo;
close DIRHANDLE;
$|++;
$|=1;
#<>;