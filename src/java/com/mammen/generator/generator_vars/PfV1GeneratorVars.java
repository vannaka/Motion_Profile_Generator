package com.mammen.generator.generator_vars;

import com.mammen.util.Mathf;
import jaci.pathfinder.Trajectory;
import javafx.beans.property.*;
import org.w3c.dom.Element;

public class PfV1GeneratorVars implements GeneratorVars
{
    public enum FitMethod
    {
        HERMITE_CUBIC( "Cubic", Trajectory.FitMethod.HERMITE_CUBIC ),
        HERMITE_QUINTIC( "Quintic", Trajectory.FitMethod.HERMITE_QUINTIC );

        private String label;
        private jaci.pathfinder.Trajectory.FitMethod pf_fitMethod;

        FitMethod( String label, Trajectory.FitMethod fitMethod )
        {
            this.label = label;
            this.pf_fitMethod = fitMethod;
        }

        @Override
        public String toString()
        {
            return label;
        }

        public Trajectory.FitMethod pfFitMethod()
        {
            return pf_fitMethod;
        }
    }

    // Singleton Instance
    private static PfV1GeneratorVars vars = null;

    // Pathfinder V1 vars
    private DoubleProperty velocity         = new SimpleDoubleProperty( 4.0 );
    private DoubleProperty accel            = new SimpleDoubleProperty( 3.0 );
    private DoubleProperty jerk             = new SimpleDoubleProperty( 5.0 );
    private Property<FitMethod> fitMethod   = new SimpleObjectProperty<>( FitMethod.HERMITE_CUBIC );
    private BooleanProperty isReversed      = new SimpleBooleanProperty( false );


    /**************************************************************************
     *   Constructor
     *************************************************************************/
    private PfV1GeneratorVars()
    {
    }

    /**************************************************************************
     * <p>Returns the PfV1GeneratorVars model.</p>
     *
     * @return The one and only instance of the PfV1GeneratorVars model.
     *************************************************************************/
    public static PfV1GeneratorVars getInstance()
    {
        if( vars == null )
        {
            vars = new PfV1GeneratorVars();
        }

        return vars;
    }

    @Override
    public void writeXMLAttributes( Element element )
    {
        element.setAttribute("fitMethod",   "" + fitMethod.getValue().name() );
        element.setAttribute("velocity",    "" + velocity.getValue()         );
        element.setAttribute("acceleration","" + accel.getValue()            );
        element.setAttribute("jerk",        "" + jerk.getValue()             );
        element.setAttribute("reversed",    "" + isReversed.getValue().toString() );
    }

    @Override
    public void readXMLAttributes( Element element )
    {
        fitMethod   .setValue( FitMethod.valueOf( element.getAttribute("fitMethod"   ) ) );
        velocity    .set( Double.parseDouble( element.getAttribute("velocity"        ) ) );
        accel       .set( Double.parseDouble( element.getAttribute("acceleration"    ) ) );
        jerk        .set( Double.parseDouble( element.getAttribute("jerk"            ) ) );
        isReversed  .set( Boolean.parseBoolean( element.getAttribute("reversed"      ) ) );
    }

    /**
     * Resets configuration to default values for the given unit.
     */
    @Override
    public void setDefaultValues()
    {
        fitMethod.setValue( FitMethod.HERMITE_CUBIC );

        switch( SharedGeneratorVars.getInstance().getUnit() )
        {
            case FEET:
                velocity.set( 4.0 );
                accel.set( 3.0 );
                jerk.set( 5.0 );
                break;

            case METERS:
                accel.set( 0.9144 );
                jerk.set( 18.288 );
                break;

            case INCHES:
                velocity.set( 48 );
                accel.set( 36 );
                jerk .set( 720 );
                break;
        }
    }

    @Override
    public void changeUnit(  Units oldUnit, Units newUnit )
    {
        // Convert each MP variable to the new unit
        double tmp_WBW = 0, tmp_WBD = 0, tmp_vel = 0, tmp_acc = 0, tmp_jer = 0;

        // convert to intermediate unit of feet
        switch( oldUnit )
        {
            case FEET:
                tmp_vel = velocity.get();
                tmp_acc = accel.get();
                tmp_jer = jerk.get();
                break;

            case INCHES:
                tmp_vel = Mathf.inchesToFeet( velocity.get() );
                tmp_acc = Mathf.inchesToFeet( accel.get() );
                tmp_jer = Mathf.inchesToFeet( jerk.get() );
                break;

            case METERS:
                tmp_vel = Mathf.meterToFeet( velocity.get() );
                tmp_acc = Mathf.meterToFeet( accel.get() );
                tmp_jer = Mathf.meterToFeet( jerk.get() );
                break;
        }

        // convert from intermediate unit of feet
        switch( newUnit )
        {
            case FEET:
                velocity    .set( tmp_vel );
                accel       .set( tmp_acc );
                jerk        .set( tmp_jer );
                break;

            case INCHES:
                velocity    .set( Mathf.round( Mathf.feetToInches( tmp_vel ),4 ) );
                accel       .set( Mathf.round( Mathf.feetToInches( tmp_acc ),4 ) );
                jerk        .set( Mathf.round( Mathf.feetToInches( tmp_jer ),4 ) );

                break;

            case METERS:
                velocity    .set( Mathf.round( Mathf.feetToMeter( tmp_vel ),4 ) );
                accel       .set( Mathf.round( Mathf.feetToMeter( tmp_acc ),4 ) );
                jerk        .set( Mathf.round( Mathf.feetToMeter( tmp_jer ),4 ) );
                break;
        }
    }


    // Getters and Setters
    public double getVelocity()
    {
        return velocity.get();
    }

    public DoubleProperty velocityProperty()
    {
        return velocity;
    }

    public void setVelocity( double velocity )
    {
        this.velocity.set( velocity );
    }

    public double getAccel()
    {
        return accel.get();
    }

    public DoubleProperty accelProperty()
    {
        return accel;
    }

    public void setAccel( double accel )
    {
        this.accel.set( accel );
    }

    public double getJerk()
    {
        return jerk.get();
    }

    public DoubleProperty jerkProperty()
    {
        return jerk;
    }

    public void setJerk( double jerk )
    {
        this.jerk.set( jerk );
    }

    public FitMethod getFitMethod()
    {
        return fitMethod.getValue();
    }

    public Property<FitMethod> fitMethodProperty()
    {
        return fitMethod;
    }

    public void setFitMethod( FitMethod fitMethod )
    {
        this.fitMethod.setValue( fitMethod );
    }

    public boolean isIsReversed()
    {
        return isReversed.get();
    }

    public BooleanProperty isReversedProperty()
    {
        return isReversed;
    }

    public void setIsReversed( boolean isReversed )
    {
        this.isReversed.set( isReversed );
    }

}