package ppciarravano.algoexplorer.export;

import java.text.DecimalFormat;
import java.util.Locale;

import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.util.FileUtility;

public class XMLExporter
{

	public static String export(ExportFrames exportFrames, String absolutePath)
	{
		String exportString = exportFrames.toString();
		Logger.log.debug("EXPORT XML: " + exportString);
		
		Locale.setDefault(Locale.US);
		DecimalFormat df = new DecimalFormat("########.##");
		
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		xml.append("<AlgoExplorerPresentation>\n");
		xml.append("	<Title><![CDATA[").append(exportFrames.getTitle()).append("]]></Title>\n");
		xml.append("	<Description><![CDATA[").append(exportFrames.getDescription()).append("]]></Description>\n");
		xml.append("	<ExportFrames>\n");
		
		int idFrame = 0;
		for (GraphFrame graphFrame : exportFrames.getFrames())
		{
			xml.append("		<GraphFrame id=\""+idFrame+"\">\n");
			xml.append("			<Title><![CDATA[").append(graphFrame.getTitle()).append("]]></Title>\n");
			xml.append("			<Description><![CDATA[").append(graphFrame.getDescription()).append("]]></Description>\n");
			xml.append("			<GraphElements>\n");
			
			for (GraphElement graphElement : graphFrame.getElements())
			{
				if (graphElement instanceof GraphCell)
				{
					GraphCell cell = (GraphCell)graphElement;
					xml.append("				");
					xml.append("<Cell id=\""+graphElement.getObjId()+"\" label=\""+graphElement.getLabel().replace("\"", "&quot;")+"\" x=\""+df.format(cell.getX())+"\" y=\""+df.format(cell.getY())+"\" width=\""+df.format(cell.getWidth())+"\" height=\""+df.format(cell.getHeight())+"\" background=\"#"+cell.getBackground()+"\" foreground=\"#"+cell.getForeground()+"\" />");
					xml.append("\n");
				}
				else if (graphElement instanceof GraphEdge)
				{
					GraphEdge edge = (GraphEdge)graphElement;
					xml.append("				");
					xml.append("<Edge id=\""+graphElement.getObjId()+"\" label=\""+graphElement.getLabel().replace("\"", "&quot;")+"\" direct=\""+edge.isDirect()+"\" xFrom=\""+df.format(edge.getxFrom())+"\" yFrom=\""+df.format(edge.getyFrom())+"\" xTo=\""+df.format(edge.getxTo())+"\" yTo=\""+df.format(edge.getyTo())+"\" />");
					xml.append("\n");
				}
			}
			
			xml.append("			</GraphElements>\n");
			xml.append("		</GraphFrame>\n");
			
			idFrame++;
		}
				
		xml.append("	</ExportFrames>\n");
		xml.append("</AlgoExplorerPresentation>\n");
				
		Logger.log.debug("XML: \n" + xml.toString());
		
		boolean writeXmlResult = FileUtility.writeTextFile(xml.toString(), absolutePath);
		Logger.log.info("XML TO FILE RESULT: " + writeXmlResult);
		
		
		/*
		//XML Example:
		
		<AlgoExplorerPresentation>
			<Title>
				<![CDATA[titolo]]>
			</Title>
			<Description>
				<![CDATA[descrizione descrizione descrizione descrizione descrizione]]>
			</Description>
			<ExportFrames>
				<GraphFrame id="">
					<Title>
						<![CDATA[titolo]]>
					</Title>
					<Description>
						<![CDATA[descrizione descrizione descrizione descrizione descrizione]]>
					</Description>
					<GraphElements>
						<Cell id="" label="" x="" y="" width="" height="" background="" foreground="" />
						<Edge id="" label="" direct="" xFrom="" yFrom="" xTo="" yTo="" />
					</GraphElements>
				</GraphFrame>
				<GraphFrame id="">
					......
				</GraphFrame>
			</ExportFrames>
		</AlgoExplorerPresentation>
		*/
		
		
		return exportString;
	}

		
}
