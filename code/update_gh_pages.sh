#!/bin/bash

echo "This script will update gh-pages and publish the JavaDoc"
echo "After publishing the JavaDoc it can take up to 10 minutes"
echo "until updates are visible on github.io"
echo ""
echo "This script needs a bash, mktemp and permissions to write"
echo "files into the temporary directory."
echo ""
echo "Press Ctrl-C to abort or enter to continue"
read

PWDDIR=$(pwd)
TMP=$(mktemp -d /tmp/tmp.XXXXXXXXXX)

# get all src dirs
SRCDIRS=$(ls -R | grep src: | tr -d ":")
LIBDIRS=$(ls -R | grep lib: | tr -d ":" )

echo "<project basedir=\".\" name=\"FUC JavaDoc Export\" default=\"export\">" > $TMP/build.xml
echo "<path id=\"sourcepath\">" >> $TMP/build.xml

for SRCDIR in $SRCDIRS
do
	echo "<pathelement location=\"$PWDDIR/$SRCDIR\" />" >> $TMP/build.xml
done

echo "</path>"  >> $TMP/build.xml

echo "<path id=\"libpath\">" >> $TMP/build.xml
for LIBDIR in $LIBDIRS
do
	JARS=$(ls $LIBDIR/*jar)
	for JAR in $JARS
	do
		echo "<pathelement location=\"$PWDDIR/$JAR\" />" >> $TMP/build.xml
	done
done

echo "</path>" >> $TMP/build.xml
echo "
<target name=\"export\" description=\"export the javadoc\">
<javadoc
   	access=\"private\"
   	author=\"true\"
   	destdir=\"$TMP/doc\"
   	doctitle=\"\${ant.project.name}\"
   	nodeprecated=\"false\"
   	nodeprecatedlist=\"false\"
   	noindex=\"false\"
   	nonavbar=\"false\"
   	notree=\"false\"
   	source=\"1.7\"
   	sourcepathref=\"sourcepath\"
	classpathref=\"libpath\"
   	splitindex=\"true\"
   	use=\"true\"
   	version=\"true\"
   	linksource=\"true\" />
</target>" >> $TMP/build.xml
echo "</project>" >> $TMP/build.xml

mkdir $TMP/doc
ant -f $TMP/build.xml

# go into the temp directory
cd $TMP

mkdir branch
cd branch
# create ant file

git init
git remote add -t gh-pages -f origin git@github.com:swp-uebersetzerbau-ss13/fuc.git
git checkout gh-pages
git pull

rm -rf doc
cp -r ../doc ./doc

git add -A
git commit -m "automatic update of generated source code page at $(date)"
git push

# restore previous path
rm -rf $TMP
cd $PWDDIR


echo ""
echo ""
echo ""
echo "JavaDoc was updated successfully"
echo "Visit: http://swp-uebersetzerbau-ss13.github.io/fuc"
echo "WARNING: It can take up to 10 minutes until your changes are visible!"
