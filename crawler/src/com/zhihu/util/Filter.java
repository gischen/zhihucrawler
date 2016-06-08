package com.zhihu.util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/24.
 */
public class Filter {

    private static String productArr[] = {
            "Product List",
            "ArcCAD",
            "ArcEditor",
            "ArcExplorer -- Java Edition",
            "ArcGIS API for Flex",
            "ArcGIS API for JavaScript",
            "ArcGIS API for Microsoft Silverlight/WPF",
            "ArcGIS API for Silverlight",
            "ArcGIS Business Analyst",
            "ArcGIS Business Analyst Online APIs",
            "ArcGIS Business Analyst Server",
            "ArcGIS Data Appliance",
            "ArcGIS Data Reviewer",
            "ArcGIS Defense Solutions",
            "ArcGIS Engine Developer Kit",
            "ArcGIS Engine Runtime",
            "ArcGIS Explorer",
            "ArcGIS Explorer Online",
            "ArcGIS for AutoCAD",
            "ArcGIS for Desktop",
            "ArcGIS for iOS",
            "ArcGIS for Local Government",
            "ArcGIS for Server",
            "ArcGIS for State Government",
            "ArcGIS Image Server",
            "ArcGIS Mobile",
            "ArcGIS OGC Interoperability Add-on",
            "ArcGIS Online",
            "ArcGIS Pro",
            "ArcGIS Server Geoportal Extension",
            "ArcGIS Solutions",
            "ArcGIS Workflow Manager",
            "ArcIMS",
            "ArcInfo Desktop",
            "ArcInfo Workstation",
            "ArcLogistics",
            "ArcLogistics Online",
            "ArcLogistics Route",
            "ArcObjects SDK for cross platform C++ Windows",
            "ArcObjects SDK for the Microsoft .NET Framework",
            "ArcPad",
            "ArcPad Application Builder",
            "ArcReader",
            "ArcSDE",
            "ArcView",
            "ArcView 3.x",
            "ArcWeb Services APIs",
            "ArcWeb Toolbar for ArcGIS",
            "Atlas GIS",
            "BusinessMAP",
            "BusinessMAP PRO",
            "Collector for ArcGIS (Android)",
            "Collector for ArcGIS (iOS)",
            "Esri Aeronautical Solution",
            "Esri Business Analyst Desktop",
            "Esri Business Analyst Online",
            "Esri Business Analyst Server",
            "Esri CityEngine",
            "Esri Defense Mapping",
            "Esri Maps for IBM Cognos",
            "Esri Maps for MicroStrategy",
            "Esri Maps for Office",
            "Esri Maps for SAP BusinessObjects",
            "Esri Nautical Solution",
            "Esri Production Mapping",
            "ESRI StreetMap Premium",
            "Explorer for ArcGIS (iOS)",
            "GIS Portal Toolkit",
            "Job Tracking for ArcGIS (JTX)",
            "MapIt",
            "Maplex",
            "MapObjects -- Java Edition",
            "MapObjects -- Windows Edition",
            "MapObjects IMS",
            "MapObjects LT",
            "MapStudio",
            "Military Overlay Editor",
            "NetEngine",
            "Operations Dashboard for ArcGIS",
            "PC ARC/INFO / DAK",
            "Portal for ArcGIS",
            "Production Line Tool Set (PLTS)",
            "RouteMAP IMS",
            "WMS and WFS Connectors for ArcIMS",
    };

    /**
     * 过滤字符串，
     * @param input
     * @return  返回符合要求的字符串，以“,”隔开
     */
    public static String  filter(String input){

        String resultStr ="";
        ArrayList list = new ArrayList();

        for (int i = 0; i <productArr.length ; i++) {
            if (input.contains(productArr[i])){
                list.add(productArr[i]);
            }
        }

        for (int i = 0; i <list.size() ; i++) {
            resultStr = resultStr + list.get(i)+",";
        }
        return resultStr;
    }
}
