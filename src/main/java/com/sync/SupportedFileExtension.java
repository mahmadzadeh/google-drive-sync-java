package com.sync;

import org.apache.commons.io.FilenameUtils;

import java.util.Optional;

/**
 * Parsed from the following page.
 *
 * http://kinlane.com/2011/02/23/file-formats-for-google-docs-api/
 */
public enum SupportedFileExtension {

    gif ("gif" ,"image/gif","Graphics Interchange Format") ,
    odt ("odt" ,"application/vnd.openxmlformats-officedocument.wordprocessingml.document","Open Document Format"),
    doc ("doc" ,"application/msword","Microsoft Word Format"),
    jpeg("jpeg" ," image/jpeg","Joint Photographic Experts Group Image Format"),
    html("html" ," text/html","HTML Format"),
    txt ("txt" ,"text/plain","TXT File"),
    pdf ("pdf" ,"application/pdf","Portable Document Format"),
    png ("png" ,"image/png","Portable Networks Graphic Image Format"),
    rtf ("rtf" ,"application/rtf","Rich Format"),
    svg ("svg" ,"", "Scalable Vector Graphics Image Format"),
    zip ("zip" ,"", "ZIP archive. Contains the images (if any) used in the document"),
    wmf ("wmf" ,"image/x-wmf","Windows Metafile"),
    ppt ("ppt" ,"application/vnd.ms-powerpoint","Powerpoint Format"),
    pps ("pps" ,"application/vnd.ms-powerpoint","Powerpoint Format"),
    swf ("swf" ,"application/x-shockwave-flash","Flash Format"),
    ods ("ods" ,"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","ODS (Open Document Spreadsheet)"),
    xls ("xls" ,"application/vnd.ms-excel","XLS (Microsoft Excel)"),
    tsv ("tsv" ,"text/tab-separated-values","TSV (Tab Separated Value)"),
    csv ("csv" ,"text/plain", "CSV (Comma Seperated Value)");

    private final String name;
    private final String mimeType;
    private final String desc;

    SupportedFileExtension(String name, String mimeType, String desc) {

        this.name = name;
        this.mimeType = mimeType;
        this.desc = desc;
    }

    public static Optional<SupportedFileExtension> fromFileName(String fileName ) {
        String extension = FilenameUtils.getExtension( fileName );

        for ( SupportedFileExtension ext : SupportedFileExtension.values() ) {
            if ( ext.isEqualTo( extension ) ) {
                return Optional.of( ext );
            }
        }

        return Optional.empty();
    }

    public boolean isEqualTo( String s ) {
        return this.name.equalsIgnoreCase( s );
    }

    public String getName() {
        return name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getDesc() {
        return desc;
    }

}
