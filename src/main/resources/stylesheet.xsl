<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

            <!-- Page Layout -->
            <fo:layout-master-set>
                <fo:simple-page-master master-name="invoicePage" page-width="8.5in" page-height="11in" margin="0.5in">
                    <fo:region-body margin-bottom="0.5in"/>
                    <fo:region-after extent="0.5in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <!-- Page Content -->
            <fo:page-sequence master-reference="invoicePage">
                <fo:flow flow-name="xsl-region-body">

                    <!-- Invoice Header -->
                    <fo:block font-size="16pt" font-weight="bold" text-align="center" space-after="10pt">
                        Invoice
                    </fo:block>

                    <fo:block font-size="10pt">
                        <xsl:text>Order ID: </xsl:text><xsl:value-of select="invoice/orderId"/>
                    </fo:block>

                    <fo:block font-size="10pt">
                        <xsl:text>Customer: </xsl:text><xsl:value-of select="invoice/customerName"/>
                    </fo:block>

                    <fo:block font-size="10pt">
                        <xsl:text>Phone: </xsl:text><xsl:value-of select="invoice/customerPhone"/>
                    </fo:block>

                    <fo:block font-size="10pt">
                        <xsl:text>Order Date: </xsl:text><xsl:value-of select="invoice/orderDate"/>
                    </fo:block>

                    <fo:block space-before="10pt" font-weight="bold">Items:</fo:block>

                    <!-- Table for Items -->
                    <fo:table border="0.5pt solid black" width="100%" space-before="5pt">
                        <fo:table-column column-width="40%"/>
                        <fo:table-column column-width="20%"/>
                        <fo:table-column column-width="20%"/>
                        <fo:table-column column-width="20%"/>
                        <fo:table-header>
                            <fo:table-row background-color="#E6E6E6">
                                <fo:table-cell><fo:block font-weight="bold">Product Name</fo:block></fo:table-cell>
                                <fo:table-cell><fo:block font-weight="bold">Barcode</fo:block></fo:table-cell>
                                <fo:table-cell><fo:block font-weight="bold">Quantity</fo:block></fo:table-cell>
                                <fo:table-cell><fo:block font-weight="bold">Price</fo:block></fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <xsl:for-each select="invoice/items/item">
                                <fo:table-row>
                                    <fo:table-cell><fo:block><xsl:value-of select="prodName"/></fo:block></fo:table-cell>
                                    <fo:table-cell><fo:block><xsl:value-of select="barcode"/></fo:block></fo:table-cell>
                                    <fo:table-cell><fo:block><xsl:value-of select="quantity"/></fo:block></fo:table-cell>
                                    <fo:table-cell><fo:block><xsl:value-of select="price"/></fo:block></fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>

                    <!-- Total Amount -->
                    <fo:block space-before="10pt" font-size="12pt" font-weight="bold">
                        <xsl:text>Total Amount: Rs.</xsl:text><xsl:value-of select="invoice/totalAmount"/>
                    </fo:block>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>
