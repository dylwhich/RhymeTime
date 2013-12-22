#!/usr/bin/perl
use strict;
# Generate dictionary from mpos.txt and mpron.txt
my %pos;
my %pron;

#todo: handle words that are multiple parts of speech.

sub readPos()
{
    # open file mpos.txt
    open(FILE, "dict/mpos.txt" ) || die "Couldn't open mpos.txt";
    print "Reading parts of speech...\n";
    
    # for each line,
    while( <FILE> ) {
        chomp;
        # if line matches [^?]\?.
        if ( m/([^\?]+)\?(.)/ ) {
            my $word = $1;
            $word =~ tr/ /_/;
            # add mapping.
            $pos{$word} = $2; 
            if( $word eq 'a' ) {
                print "Mapping for a: $2\n";
            }
        }
    }

    # close file.
    close FILE;
}

sub uniq(@) {
     return keys %{{ map { $_ => 1 } @_ }};
}

sub readPron()
{
    # open file mpron.txt
    open(FILE, "dict/mpron.txt" ) || die "Couldn't open mpron.txt";
    print "Reading pronunciations...\n";
    
    # for each line,
    while( <FILE> ) {
        chomp;
        # if line matches \w+ .*\
        if ( m/^([A-Za-z\-_\/\']+) (.+)/ ) {
            # add mapping.
            $pron{$1} = $2;
        }
        if( $1 eq 'a' ) {
            print "Mapping for a: $2\n";
        }
    }

    # close file.
    close FILE;
}

sub writeData()
{
    print("Writing data....\n");
    # open dict.txt
    open(FILE, ">dict.txt") || die "Couldn't open dict.txt";
    # for each entry in either dictionary,
    my $count = 0;
    for ( sort( uniq( (keys %pron, keys %pos) ) ) ) {
        # If both entries exist,
        my $wpron = $pron{$_};
        my $wpos = $pos{$_};
        if ( $wpron && $wpos ) {
            # output a record.
            print FILE "$_ $wpos $wpron\n";
            $count++;
        }
    }

    print "Write $count words.\n";

    # close
    close FILE;
}

sub main()
{
    # read part-of-speech file
    readPos();

    # read pronunciation file.
    readPron();

    # write data.
    writeData();
}

main();
